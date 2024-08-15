import '../style/keypad.css';

export default function SecureKeypad({ keypad, onKeyPressed, onClearAll, onDeleteOne, onConfirm }) {
    return (
        <>
            <div className="table-container">
                <table className="dynamic-table">
                    <tbody>
                    {[...Array(3)].map((_, rowIndex) => (
                        <tr key={rowIndex}>
                            {[...Array(4)].map((_, colIndex) => {
                                const index = rowIndex * 4 + colIndex; // numberHashArray의 인덱스 계산
                                return (
                                    <td
                                        key={colIndex}
                                        className="key-cell"
                                        onClick={() => onKeyPressed(index)}
                                    >
                                        <div
                                            className="image-piece"
                                            style={{
                                                backgroundImage: `url(data:image/png;base64,${keypad})`,
                                                backgroundPosition: `-${colIndex * 50}px -${rowIndex * 50}px`,
                                                backgroundSize: '200px 150px', // 이미지 전체 크기
                                                width: '50px',
                                                height: '50px',
                                            }}
                                        >
                                        </div>
                                    </td>
                                );
                            })}
                        </tr>
                    ))}
                    {/* 버튼을 포함한 행 추가 */}
                    <tr>
                        <td className="action-cell" onClick={onClearAll}>비우기</td>
                        <td className="action-cell" onClick={onDeleteOne}>지우기</td>
                        <td className="action-cell confirm-cell" colSpan="2" onClick={onConfirm}>확인</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </>
    );
}
