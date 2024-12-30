import React from 'react';
import {
   View, 
   Text, 
   TouchableOpacity, 
   StyleSheet 
} from 'react-native';
import { useRouter, usePathname } from 'expo-router'; 
import { COLORS } from '@/src/theme/theme'; 
import AntDesign from 'react-native-vector-icons/AntDesign';
import Feather from 'react-native-vector-icons/Feather';

const BottomNavigationBar = () => {
  const router = useRouter();
  const currentRoute = usePathname(); 

  const tabs = [
    { key: '/', icon: <AntDesign name="home" size={24} />, label: 'Home' },
    { key: '/SearchScreen', icon: <AntDesign name="search1" size={24} />, label: 'Search' },
    { key: '/Profile', icon: <Feather name="user" size={24} />, label: 'User' },
  ];

  return (
    <View style={styles.container}>
      {tabs.map((tab) => {
        const isActive = currentRoute === tab.key;

        return (
          <TouchableOpacity
            key={tab.key} 
            style={styles.contentContainer}
            onPress={() => {
              router.push(tab.key); 
            }}
          >
            <View style={{ marginBottom: 5 }}>
              {React.cloneElement(tab.icon, {
                color: isActive ? '#0072bc' : '#abb7c2',
              })}
            </View>
            <Text style={{ color: isActive ? '#0072bc' : '#abb7c2' }}>
              {tab.label}
            </Text>
          </TouchableOpacity>
        );
      })}
    </View>
  );
};

export default BottomNavigationBar;

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    alignItems: 'center',
    paddingHorizontal: 30,
    backgroundColor: COLORS.White,
    height: 80,
    position: 'absolute',
    bottom: 0,
  },
  contentContainer: {
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'flex-start',
  },
});
