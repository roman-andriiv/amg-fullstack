import {createContext, useContext, useState} from "react";
import {login as performLogin} from "../../services/client.js";


const AuthContext = createContext({})
const AuthProvider = ({children}) => {

    const [customer, setCustomer] = useState(null);
    const logIn = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword)
                .then(res => {
                    const jwtToken = res.headers["authorization"];
                    localStorage.setItem("access_token", jwtToken)
                    console.log(jwtToken)
                    setCustomer({
                        ...res.data.customerDto
                    })
                    resolve(res)
                })
                .catch(err => {
                    reject(err)
                })
        })
    }

    const logOut = () => {
        localStorage.removeItem("access_token")
        setCustomer(null)
    }

    return (
        <AuthContext.Provider value={{customer, logIn, logOut}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)
export default AuthProvider