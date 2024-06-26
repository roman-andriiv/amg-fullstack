import {createContext, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../../services/client.js";
import {jwtDecode} from "jwt-decode";


const AuthContext = createContext({})
const AuthProvider = ({children}) => {

    const [customer, setCustomer] = useState(null);
    useEffect(() => {
        let token = localStorage.getItem("access_token")
        if (token) {
            token = jwtDecode(token)
            setCustomer({
                username: token.sub,
                roles: token.scopes
            })
        }
    }, []);
    const logIn = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword)
                .then(res => {
                    const jwtToken = res.headers["authorization"];
                    localStorage.setItem("access_token", jwtToken)
                    console.log(jwtToken)

                    const decodedToken = jwtDecode(jwtToken)
                    setCustomer({
                        name: decodedToken.sub,
                        roles: decodedToken.scopes
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

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logOut()
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{customer, logIn, logOut, isCustomerAuthenticated}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)
export default AuthProvider