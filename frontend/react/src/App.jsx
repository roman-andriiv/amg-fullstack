import {Button} from "@chakra-ui/react";
import SidebarWithHeader from "./shared/SideBar.jsx"
const App = ()=>{
    return(
        <SidebarWithHeader>
            <Button colorScheme='teal' variant='solid'>Click me</Button>
        </SidebarWithHeader>
    )
}

export default App