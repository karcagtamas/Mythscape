import { Button, TextField, Typography } from '@mui/material';
import './page.scss';

const LoginPage = () => {
  return (
    <div className="login-form">
      <div>
        <Typography variant="h5" color="primary" fontWeight={'bold'}>
          Login
        </Typography>
      </div>
      <div className="fields">
        <TextField size="small" label="User name"></TextField>
        <TextField label="Password"></TextField>
      </div>
      <div className="action">
        <Button variant="contained">Login</Button>
      </div>
      <div className="footer">
        <Button>Sign Up instead</Button>
      </div>
    </div>
  );
};

export default LoginPage;
