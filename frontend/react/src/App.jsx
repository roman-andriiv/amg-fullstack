import {Spinner, Text} from "@chakra-ui/react";
import SidebarWithHeader from "./shared/SideBar.jsx"
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";

const App = () => {

    const [customers, setCustomers] = useState([])
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        setLoading(true)
        getCustomers().then(resp => {
            setCustomers(resp.data)
        }).catch(err => {
            console.log(err)
        }).finally(() => {
            setLoading(false)
        })
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No customers available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            {customers.map((customer, index) => (
                <p key={index}>{customer.name}</p>
            ))}
        </SidebarWithHeader>
    )
}

export default App