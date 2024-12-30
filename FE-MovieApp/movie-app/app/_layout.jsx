import React from 'react';
import { Stack } from 'expo-router'; 
import { SafeAreaView } from 'react-native-safe-area-context'; 
import { StyleSheet } from 'react-native';

const RootLayout = () => {
  return (
    <SafeAreaView style={styles.container} edges={['left', 'right', 'bottom']}>
      <Stack>
        <Stack.Screen name="index" options={{ headerShown: false }}/>
        <Stack.Screen name="SearchScreen" options={{ headerShown: false }}/>
        <Stack.Screen name="Profile" options={{ headerShown: false }}/>
        <Stack.Screen name="DetailAboutScreen" options={{ headerShown: false }}/>
        <Stack.Screen name="Category" options={{ headerShown: false }}/>
        <Stack.Screen name="MoviesDetailScreen" options={{ headerShown: false }}/>
      </Stack>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default RootLayout;
