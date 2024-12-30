import React from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  TouchableOpacity,
  Dimensions
} from 'react-native'; 
import { COLORS, SPACING } from '@/src/theme/theme';
import AntDesign from 'react-native-vector-icons/AntDesign';
import Svg, { Rect, Line, Defs, LinearGradient, Stop } from 'react-native-svg';
import { useRouter } from 'expo-router';
import Feather from 'react-native-vector-icons/Feather';

const { width,height } = Dimensions.get('window');

const Header = ({ title }) => {
  const router = useRouter();
  const navigateToBack = () => {
    router.back(); 
  };

  return (
    <View style={styles.searchBar}>
        <Svg style={styles.svgBackground}>
            <Defs>
              <LinearGradient id="gradient" x1="0" y1="0" x2="0" y2="1">
                <Stop offset="0%" stopColor={COLORS.Yellow} stopOpacity="1" />
                <Stop offset="100%" stopColor={COLORS.Yellow} stopOpacity="1" />
              </LinearGradient>
            </Defs>
            <Rect x="0" y="0" width="100%" height="100%" fill="url(#gradient)" />

            {Array.from({ length: 15 }).map((_, index) => {
              // const randomLength = Math.random() * 100; 
              return (
                <Line
                  key={index}
                  x1="0"
                  y1={index * 8}
                  x2={index * 8}
                  // x2={randomLength}
                  y2={index * 8}
                  stroke="rgba(255, 255, 255, 0.5)"
                  strokeWidth="2"
                  opacity={0.5}
                />
              );
            })}
          </Svg>
        <TouchableOpacity 
        style={styles.backButton}
        onPress={navigateToBack}
        >
          <AntDesign 
            name="arrowleft"
            size={24}
            color={COLORS.Blue}
          />
        </TouchableOpacity>

        {/* Tiêu đề */}
        <View style={{ height: 100, justifyContent: 'center', alignItems: 'center' }}>
          <Svg style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }}>
            <Line 
                  x1="25" 
                  y2="45"  
                  stroke="#fff225" 
                  strokeWidth="2"
                  opacity={0.5}
                />
            <Line 
                  x1="10"
                  y2="20"  
                  stroke="#fff225" 
                  strokeWidth="2" 
                  opacity={0.5}
                />
            </Svg>
          <Text style={styles.title}>{title}</Text>
        </View>

        {/* Nút Search và  Avatar  */}
        <View style={styles.iconContainer}>
            <View style={styles.moreContaier}>
              <TouchableOpacity>
                <Feather name="more-horizontal" size={20} color="#006bb3" />
              </TouchableOpacity>
              <Text style={styles.moreText}>|</Text>
              <TouchableOpacity>
                <Feather name="x" size={20} color="#006bb3" />
              </TouchableOpacity>
            </View>
            {/* <Svg style={styles.crossLines}>
              <Line 
                    x1="45" 
                    y2="65"  
                    stroke="#fff225" 
                    strokeWidth="2" 
                    opacity={0.5}
              />
              <Line 
                    x1="25"
                    y2="40"  
                    stroke="#fff225" 
                    strokeWidth="2" 
                    opacity={0.5}
              />
            </Svg> */}
          </View>
    </View>
  );
}

export default Header

const styles = StyleSheet.create({

  

  searchBar: {
    width:"100%",
    height:height * 0.13,
    backgroundColor: COLORS.Yellow,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    position: 'relative',
    paddingHorizontal: SPACING.space_16,
    paddingTop: SPACING.space_36,
    overflow: 'hidden' ,
    top: 0,
    position: 'fixed',
  },
  
  
  backButton: {
    width: width * 0.1,  
    height: width * 0.1,
    backgroundColor: '#FFFFFF',
    borderRadius: 40,
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 10,
    marginTop: height * 0.005,
  },
  
  
  svgBackground: {
    ...StyleSheet.absoluteFillObject, 
    zIndex: 1,
    marginLeft: -5,
    width: width * 0.15, 
    height: height * 0.13, 
    borderRadius: "100%",
  },
  
  
  title: {
    paddingLeft: 50,
    fontSize:  width * 0.05,
    fontWeight: 'bold',
    color: COLORS.Blue,
    textAlign: 'center',
    marginTop: height * 0.005,

  },
  
  
  iconContainer:{
    flexDirection: 'row',  
    alignItems: 'center',  
    marginLeft: 10,
    marginTop: height * 0.005,

  },
    moreContaier: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      height: width * 0.09,
      width: width * 0.2,
      backgroundColor: '#f2f2f2',
      paddingHorizontal: 10,
      borderRadius: 40,
      opacity: 0.8,
    },

    moreText: {
      color: '#737373',
      fontSize: 16,
      opacity: 0.4,
      fontWeight: 'semibold',
    },

})

