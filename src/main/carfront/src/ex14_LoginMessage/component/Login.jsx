import { useState } from "react";
import { SERVER_URL } from "./constants";
import { Button, TextField, Stack, Snackbar } from "@mui/material";
import CarList from "./CarList";

const Login = ()=>{
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("");
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
            }else{
                setMessage("Not Received Token! Login Failed: Check your username and password");
                setOpen(true);
            }
        })
        .catch((e)=>{
            console.log(e)
            setMessage(e.message || String(e));
            setOpen(true);
        });
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
                <Snackbar open={open} autoHideDuration={3000}
                        onClose={()=>setOpen(false)}
                        message = {message} />
            </div>
        )        
    }
}

export default Login;