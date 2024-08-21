"use client";

import { useState, useEffect } from 'react';
import axios from "axios";
import forge from 'node-forge';
import { JSEncrypt } from "jsencrypt";

export default function useSecureKeypad() {
  const [keypad, setKeypad] = useState(null);
  const [numberHashArray, setNumberHashArray] = useState([]); // hashArray를 상태로 저장
  const [userInput, setUserInput] = useState([]);
  const [id, setId] = useState(''); // id를 상태로 저장
  const [publicKey, setPublicKey] = useState(null); // 공개키를 상태로 저장

  useEffect(() => {
    // 공개키를 불러옵니다.
    const fetchPublicKey = async () => {
      try {
        const response = await fetch('/public_key.pem');
        const pem = await response.text();
        const parsedKey = forge.pki.publicKeyFromPem(pem);
        setPublicKey(parsedKey);
      } catch (error) {
        console.error('Error fetching public key', error);
      }
    };

    fetchPublicKey();
  }, []);

  const getSecureKeypad = async () => {
    try {
      const response = await axios.post('/api/keypad/create'); // 백엔드의 API 엔드포인트에 맞춤
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

  const encryptData = (data) => {
    if (!publicKey) {
      console.error('Public key not loaded');
      return null;
    }

    // PEM 형식의 공개 키 문자열로 변환
    const publicKeyPem = forge.pki.publicKeyToPem(publicKey);

    // JSEncrypt를 사용하여 암호화
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKeyPem);
    return encrypt.encrypt(data);
  };

  const onConfirm = async (input = []) => {
    if (!Array.isArray(input)) {
      input = userInput;
    }

    const inputString = input.join('');
    const encryptedInput = encryptData(inputString); // 데이터를 암호화합니다.
    alert(inputString);
    alert(encryptedInput);
    try {
      const response = await axios.post('/api/keypad/input', { // 백엔드의 API 엔드포인트에 맞춤
        id, // 키패드의 고유 ID
        encStr: encryptedInput // 암호화된 데이터를 전송합니다.
      });

      if (response.status === 200) {
        alert(response.data);
        window.location.reload(); // 페이지 새로고침
      } else {
        alert('키패드 입력 처리 중 오류가 발생했습니다.');
      }
    } catch (error) {
      console.error('Error submitting the keypad input', error);
      alert('키패드 입력 처리 중 오류가 발생했습니다.');
    }
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
