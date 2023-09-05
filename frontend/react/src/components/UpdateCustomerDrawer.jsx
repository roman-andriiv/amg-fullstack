import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import {CloseIcon, EditIcon} from '@chakra-ui/icons'
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";

const UpdateCustomerDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            leftIcon={<EditIcon/>}
            colorScheme={"teal"}
            rounded={"full"}
            _hover={{
                transform: 'translateY(-5px)',
                boxShadow: 'lg'
            }}
            onClick={onOpen}
        >
            Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Update customer</DrawerHeader>

                <DrawerBody>
                    <UpdateCustomerForm
                        fetchCustomers={fetchCustomers}
                        initialValues={initialValues}
                        customerId={customerId}
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        leftIcon={<CloseIcon/>}
                        colorScheme={"teal"}
                        onClick={onClose}>
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default UpdateCustomerDrawer;
