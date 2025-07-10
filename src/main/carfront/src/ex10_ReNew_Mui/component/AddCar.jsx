import { useState } from "react";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import { Button, TextField, Stack } from "@mui/material";

const AddCar = (props)=>{
    const { addCar } = props;

    const [open, setOpen] = useState(false);
    const [car, setCar] = useState({
        brand : "",
        model : "",
        color : "",
        registrationNumber : "",
        modelYear : "",
        price : ""
    });

    const handleClickOpen = ()=>setOpen(true);
    const handleClickClose = ()=>setOpen(false);
    const handleChange = (e)=>setCar({...car, [e.target.name] : e.target.value});
    const handleSave = ()=>{
        addCar(car);
        handleClickClose();
    }

    return (
        <div>
            <Button variant="contained" onClick={handleClickOpen}>
                New Car
            </Button>
            <Dialog open={open} onClose={handleClickClose}>
                <DialogTitle>New Car</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} mt={1}>
                        <TextField label="Brand" name="brand" value={car.brand} onChange={handleChange} variant="standard" autoFocus /><br/>
                        <TextField label="Model" name="model" value={car.model} onChange={handleChange} variant="standard"/><br/>
                        <TextField label="Color" name="color" value={car.color} onChange={handleChange} variant="standard"/><br/>
                        <TextField label="RegNum" name="registrationNumber" value={car.registrationNumber} onChange={handleChange} variant="standard"/><br/>
                        <TextField label="ModelYear" name="modelYear" value={car.modelYear} onChange={handleChange} variant="standard"/><br/>
                        <TextField label="Price" name="price" value={car.price} onChange={handleChange} variant="standard"/><br/>
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClickClose}>Cancel</Button>
                    <Button onClick={handleSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </div>
    )
}

export default AddCar;
