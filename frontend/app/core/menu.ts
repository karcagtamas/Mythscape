type MenuItem = {
  key: string;
  text: string;
  href: string;
  icon?: string;
};

export const MENU: MenuItem[] = [
  { key: 'campaigns', text: 'Campaigns', href: '/campaigns', icon: '' },
];

type SettingsMenuItem = {
  key: string;
  text: string;
  icon?: string;
};

export const PROFILE_MENU: SettingsMenuItem[] = [
  {
    key: 'settings',
    text: 'Settings',
  },
  {
    key: 'logout',
    text: 'Logout',
  },
];
