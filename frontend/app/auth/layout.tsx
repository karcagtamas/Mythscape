import { Paper, Typography } from '@mui/material';
import './layout.scss';

type Props = {
  children: React.ReactNode;
};

const AuthLayout: React.FC<Props> = (props: Props) => {
  return (
    <div className="layout auth-layout">
      <header>
        <Typography variant="h4" fontWeight={'bold'}>
          Mythscape
        </Typography>
      </header>
      <div className="layout-body">
        <Paper sx={{ padding: '0.4rem' }}>{props.children}</Paper>
      </div>
    </div>
  );
};

export default AuthLayout;
