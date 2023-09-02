import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    Input,
    useDisclosure
} from "@chakra-ui/react";
import {AddIcon} from '@chakra-ui/icons'

const DrawerForm = () => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            leftIcon={<AddIcon/>}
            colorScheme={"teal"}
            onClick={onOpen}>
            Create new customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Create your account</DrawerHeader>

                <DrawerBody>
                    <form
                        id='my-form'
                        onSubmit={(e) => {
                            e.preventDefault()
                            console.log('submitted')
                        }}
                    >
                        <Input name='nickname' placeholder='Type here...'/>
                    </form>
                </DrawerBody>

                <DrawerFooter>
                    <Button type='submit' form='my-form'>
                        Save
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default DrawerForm;
