import '../style/keypad.css';

export default function KeypadUserInput({ userInput }) {
    return (
        <>
            {/*<div className="text-display">*/}
            {/*    {userInput.map((input, index) => (*/}
            {/*        <div key={index} className="text-line">*/}
            {/*            {input}*/}
            {/*        </div>*/}
            {/*    ))}*/}
            {/*</div>*/}
            <div className="input-group-style">
                {[...Array(6)].map((_, index) => (
                    <div
                        key={index}
                        className={`input-circle ${index < userInput.length ? 'filled' : ''}`}
                    ></div>
                ))}
            </div>
        </>
    );
}
