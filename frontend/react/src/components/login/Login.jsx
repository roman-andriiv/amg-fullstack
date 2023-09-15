'use client'

import {Alert, AlertIcon, Box, Button, Flex, FormLabel, Heading, Image, Input, Stack, Text,} from '@chakra-ui/react'
import {Form, Formik, useField} from "formik";
import * as Yup from 'yup'
import {useAuth} from "../context/AuthContext.jsx";
import React from "react";
import {errorNotification} from "../../services/notification.js";

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

const LoginForm = () => {
    const {login} = useAuth()

    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .min(6, "Password cannot be less than 6 characters")
                        .required("Password is required")
                })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true)
                login(values)
                    .then(res => {
                        //TODO: navigate to dashboard
                        console.log("Success login", res)
                    })
                    .catch(err => {
                        console.log(err)
                        errorNotification(err.code, err.response.data.message)
                    })
                    .finally(() => {
                        setSubmitting(false)
                    })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={6}>
                        <MyTextInput
                            label={"Email"}
                            name={"username"}
                            type={"email"}
                            placeholder={"hello@example.com"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"Type your password"}
                        />
                        <Button isDisabled={!isValid || isSubmitting}
                                type={"submit"}
                                colorScheme={"teal"}>
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

const Login = () => {

    useAuth()

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        alignSelf={"center"}
                        borderRadius='full'
                        boxSize='180px'
                        src='https://img.freepik.com/free-vector/technology-logo-modern-business-branding-digital-company-startup-vector_53876-136234.jpg?w=740&t=st=1693404919~exp=1693405519~hmac=8e2ab32ecce18be9338061592386680bbee3ef220556e4a91232ff4bf97863ec'
                        alt='Logo'
                        mb={30}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                    <LoginForm/>
                </Stack>
            </Flex>
            <Flex flex={1} p={10}
                  flexDirection={"column"}
                  alignItems={"center"}
                  justifyContent={"center"}
                  bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}
            >
                <Text fontSize={"4xl"} color={'white'} fontWeight={"bold"} mb={5}>
                    Customer Management System
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
                    }
                />
            </Flex>
        </Stack>
    )
}
export default Login;