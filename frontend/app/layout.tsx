'use client';

import './global.scss';
import React, { useState } from 'react';
import { AppRouterCacheProvider } from '@mui/material-nextjs/v15-appRouter';
import { Roboto } from 'next/font/google';
import {
  AppBar,
  Avatar,
  Box,
  Button,
  Container,
  IconButton,
  Menu,
  MenuItem,
  ThemeProvider,
  Toolbar,
  Tooltip,
  Typography,
} from '@mui/material';
import theme from './theme';
import { AdbOutlined } from '@mui/icons-material';
import Link from 'next/link';

const roboto = Roboto({
  weight: ['300', '400', '500', '700'],
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-roboto',
});

const RootLayout = ({ children }: Readonly<{ children: React.ReactNode }>) => {
  const [userAnchor, setUserAnchor] = useState<null | HTMLElement>(null);

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setUserAnchor(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setUserAnchor(null);
  };

  return (
    <html lang="en">
      <body className={roboto.variable}>
        <AppRouterCacheProvider options={{ enableCssLayer: true }}>
          <ThemeProvider theme={theme}>
            <AppBar position="static">
              <Container maxWidth="xl">
                <Toolbar disableGutters>
                  <AdbOutlined sx={{ mr: 1, display: 'flex' }} />
                  <Typography
                    variant="h6"
                    noWrap
                    fontWeight="600"
                    sx={{ display: 'flex' }}
                  >
                    <Link href={'/'}>Mythscape</Link>
                  </Typography>
                  <Box
                    sx={{ flexGrow: 1, display: 'flex', gap: '1rem', pl: 3 }}
                  >
                    <Link href={'/campaigns'}>
                      <Button sx={{ color: 'var(--white)' }}>Campaigns</Button>
                    </Link>
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
                      <MenuItem onClick={handleCloseUserMenu}>
                        <Typography sx={{ textAlign: 'center' }}>
                          Settings
                        </Typography>
                      </MenuItem>

                      <MenuItem onClick={handleCloseUserMenu}>
                        <Typography sx={{ textAlign: 'center' }}>
                          Logout
                        </Typography>
                      </MenuItem>
                    </Menu>
                  </Box>
                </Toolbar>
              </Container>
            </AppBar>
            {children}
          </ThemeProvider>
        </AppRouterCacheProvider>
      </body>
    </html>
  );
};

export default RootLayout;
