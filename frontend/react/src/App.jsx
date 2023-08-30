import {useState} from "react";
import UserProfile from "./UserProfile.jsx";

const users = [
    {
        name: "Jamila",
        age: 22,
        gender: "FEMALE",
    },
    {
        name: "Ana",
        age: 45,
        gender: "FEMALE",
    },
    {
        name: "Alex",
        age: 18,
        gender: "MALE",
    },
    {
        name: "Bilal",
        age: 27,
        gender: "MALE",
    },
    {
        name: "Bob",
        age: 27,
        gender: "MALE",
    },
]

const UserProfiles = ({users}) => (
    <div>
        {users.map((user, index) => (
            <UserProfile
                key={index}
                name={user.name}
                age={user.age}
                gender={user.gender}
                imageNumber={index}
            />
        ))}
    </div>
)

function App() {
    const [counter, setCounter] = useState(0)
    return (
        <div>
            <h1>{counter}</h1>
            <button onClick={() => setCounter(prevCounter => ++prevCounter)}>
                Increment counter
            </button>
            <UserProfiles users={users}/>
        </div>
    )
}

export default App
