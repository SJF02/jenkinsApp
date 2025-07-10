// npm install @mui/material @emotion/react @emotion/styled 설치
import "../App.css";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";

const MainApp = () => {
    return (
        <div className="App">
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6">
                        Carshop
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    )
}

export default MainApp;