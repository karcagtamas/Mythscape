import Link from 'next/link';
import './AppBar.scss';
import Button from './buttons/Button';

type Props = {
  title: string;
  buttons?: React.ReactNode;
};

const AppBar: React.FC<Props> = (props: Props) => {
  return (
    <div className="app-bar">
      <div className="app-bar-title">
        <Link href={'/'}>{props.title}</Link>
      </div>
      <div className="app-bar-buttons">
        <Button />
      </div>
      <div className="spacer"></div>
    </div>
  );
};

export default AppBar;
