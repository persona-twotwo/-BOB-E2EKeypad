"use client";

import { useState } from 'react';
import axios from "axios";

export default function useSecureKeypad() {
  const [keypad, setKeypad] = useState(null);
  const [numberHashArray, setNumberHashArray] = useState([]); // hashArray를 상태로 저장
  const [userInput, setUserInput] = useState([]);
  const [id, setId] = useState(''); // id를 상태로 저장

  const getSecureKeypad = async () => {
    try {
      const response = await axios.post('/api/createKeypad');
      console.log(response);
      const base64Image = response.data['keypadPhoto'];
      const hashArray = response.data['numberHashArray']; // 서버에서 받아온 hashArray
      const idValue = response.data['id']; // 서버에서 받아온 id
      setKeypad(base64Image);
      setNumberHashArray(hashArray); // hashArray를 상태로 저장
      setId(idValue); // id를 상태로 저장
    } catch (error) {
      console.error('Error fetching the keypad', error);
    }
  };

  const onKeyPressed = (index) => {
    const value = numberHashArray[index]; // 상태로 저장된 numberHashArray 사용
    if (value && userInput.length < 6) {
      const updatedInput = [...userInput, value];
      setUserInput(updatedInput);

      if (updatedInput.length === 6) {
        onConfirm(updatedInput); // 6자리가 되면 자동으로 onConfirm 호출
      }
    }
  };

  const onClearAll = () => {
    setUserInput([]);
  };

  const onDeleteOne = () => {
    if (userInput.length > 0) {
      setUserInput(userInput.slice(0, -1));
    }
  };

  const onConfirm = (input = []) => {
    if (!Array.isArray(input)) {
      input = userInput;
    }
    const inputString = input.map((input, index) => `키 입력 ${index + 1}: ${input}`).join('\n');
    alert(`키패드 고유번호: ${id}\n\n${inputString}`);
    window.location.reload(); // 페이지 새로고침
  };

  return {
    states: {
      keypad,
      userInput,
      id, // id를 상태로 반환
    },
    actions: {
      getSecureKeypad,
      onKeyPressed,
      onClearAll,
      onDeleteOne,
      onConfirm,
    }
  };
}
