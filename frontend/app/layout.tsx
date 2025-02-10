import "./global.scss";
import React from "react";
import localFont from 'next/font/local';

const roboto = localFont({
  src: [
    {
      path: './fonts/roboto-regular.woff2',
      weight: '400',
      style: 'normal',
    },
    {
      path: './fonts/roboto-italic.woff2',
      weight: '400',
      style: 'italic',
    },
    {
      path: './fonts/roboto-bold.woff2',
      weight: '700',
      style: 'normal',
    },
  ]
})

const RootLayout = ({children}: Readonly<{ children: React.ReactNode }>) => {
  return (
    <html lang="en" className={roboto.className}>
      <body>
      {children}
      </body>
    </html>
  );
}

export default RootLayout;