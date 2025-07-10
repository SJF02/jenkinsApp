import { useState } from "react";
import { SERVER_URL } from "./constants";
import { Button, TextField, Stack } from "@mui/material";
import CarList from "./CarList";

const Login = ()=>{
    const [user, setUser] = useState({
        username: "",
        password: ""
    });

    const bAuth = sessionStorage.getItem("jwt") !== null ? true : false;
    const [isAuthenticated, setAuth] = useState(bAuth);

    const handleChange = (e)=>setUser({...user, [e.target.name] : e.target.value});

    const login = ()=>{
        fetch(SERVER_URL + "login", {
            method: "POST",
            headers: {"Content-Type" : "application/json"},
            body: JSON.stringify(user)
        })
        .then((resp)=>{
            const jwtToken = resp.headers.get("Authorization");
            if(jwtToken !== null){
                sessionStorage.setItem("jwt", jwtToken);
                setAuth(true);
            }
        })
        .catch((e)=>console.log(e));
    }

    const notAuth = ()=>{
        setAuth(false);
        sessionStorage.removeItem("jwt");
    }

    if(isAuthenticated){
        return <CarList notAuth={notAuth} />
    }else{
        return(
            <div>
                <Stack spacing={2} alignItems="center" mt={2}>
                    <TextField name="username" label="username" onChange={handleChange} />
                    <TextField name="password" label="password" type="password" onChange={handleChange} />
                    <Button variant="outlined" color="primary" onClick={login}>Login</Button>
                </Stack>
            </div>
        )        
    }
}

export default Login;