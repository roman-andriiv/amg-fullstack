import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {registerCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";

const MyTextInput = ({label, ...props}) => {

    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};
const MySelect = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const CreateCustomerForm = ({onSuccess}) => {
    return (
        <Formik
            initialValues={{
                name: '',
                email: '',
                age: null,
                gender: '',
                password: ''
            }}
            validationSchema={Yup.object({
                name: Yup.string()
                    .max(15, 'Must be 15 characters or less')
                    .required('Name is required'),
                email: Yup.string()
                    .email('Invalid email address')
                    .required('Email is required'),
                age: Yup.number()
                    .min(16, 'Must be at least 16 years of age')
                    .max(100, 'Must be less 100 years of age')
                    .required('Age is required'),
                gender: Yup.string()
                    .oneOf(
                        ['MALE', 'FEMALE'],
                        'Invalid gender'
                    )
                    .required('Gender is required'),
                password: Yup.string()
                    .min(6, 'Must be 6 characters or more')
                    .required('Password is required'),
            })}
            onSubmit={(customer, {setSubmitting}) => {
                setSubmitting(true)
                registerCustomer(customer).then(res => {
                    console.log(res)
                    successNotification("Customer saved", `${customer.name} was successfully saved`)
                    onSuccess(res.headers["authorization"])
                }).catch(err => {
                    console.log(err)
                    errorNotification(err.code, err.response.data.message)
                }).finally(() => {
                    setSubmitting(false)
                })
            }}
        >

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={'24px'}>
                        <MyTextInput
                            label="Name"
                            name="name"
                            type="text"
                            placeholder="Jane"
                        />

                        <MyTextInput
                            label="Email"
                            name="email"
                            type="email"
                            placeholder="jane@example.com"
                        />

                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                            placeholder="My strong password"
                        />

                        <MyTextInput
                            label="Age"
                            name="age"
                            type="number"
                            placeholder="16"
                        />

                        <MySelect label="Gender" name="gender">
                            <option value="">Select gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </MySelect>

                        <Button isDisabled={!isValid || isSubmitting} type="submit" colorScheme={"teal"}>
                            Register
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};
export default CreateCustomerForm;