'use client';

import { AdbOutlined } from '@mui/icons-material';
import {
  AppBar,
  Avatar,
  Box,
  Button,
  IconButton,
  Link,
  Menu,
  MenuItem,
  Toolbar,
  Tooltip,
  Typography,
} from '@mui/material';
import { MENU, PROFILE_MENU } from '../core/menu';
import { useState } from 'react';

const AppLayout = () => {
  const [userAnchor, setUserAnchor] = useState<null | HTMLElement>(null);

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setUserAnchor(event.currentTarget);
  };

  const handleCloseUserMenu = (key: string) => {
    if (key === 'settings') {
      // TODO: Open settings page/dialog
    } else if (key === 'logout') {
      // TODO: Do logout
    }

    setUserAnchor(null);
  };

  return (
    <AppBar position="static">
      <Toolbar disableGutters sx={{ paddingX: '1rem' }}>
        <AdbOutlined sx={{ mr: 1, display: 'flex' }} />

        <Typography noWrap fontWeight="600" sx={{ display: 'flex' }}>
          <Link href={'/'}>Mythscape</Link>
        </Typography>

        <Box sx={{ flexGrow: 1, display: 'flex', gap: '1rem', pl: 3 }}>
          {MENU.map((item) => {
            return (
              <Link key={item.key} href={item.href}>
                <Button sx={{ color: 'var(--white)' }}>{item.text}</Button>
              </Link>
            );
          })}
        </Box>

        <Box sx={{ flexGrow: 0 }}>
          <Tooltip title="Open Settings">
            <IconButton onClick={handleOpenUserMenu}>
              <Avatar></Avatar>
            </IconButton>
          </Tooltip>
          <Menu
            anchorEl={userAnchor}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            keepMounted
            transformOrigin={{ vertical: 'top', horizontal: 'right' }}
            open={Boolean(userAnchor)}
            onClose={handleCloseUserMenu}
          >
            {PROFILE_MENU.map((item) => {
              return (
                <MenuItem
                  key={item.key}
                  onClick={() => handleCloseUserMenu(item.key)}
                >
                  <Typography sx={{ textAlign: 'center' }}>
                    {item.text}
                  </Typography>
                </MenuItem>
              );
            })}
          </Menu>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default AppLayout;
