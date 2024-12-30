import { View, Text, StyleSheet, TouchableOpacity, Image, TextInput, ScrollView, Dimensions } from 'react-native';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'expo-router';
import Header from '@/app/components/Header';
import { COLORS, SPACING } from '@/src/theme/theme';
import Footer from '@/app/components/Footer';
import { favoritehMovies, updateFavorite } from '@/src/api/api';
import AntDesign from '@expo/vector-icons/AntDesign';
import Ionicons from 'react-native-vector-icons/Ionicons';

const { width: screenWidth } = Dimensions.get('window');

const Profile = () => {
  const router = useRouter();

  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadFavorites = async () => {
      setLoading(true); 
      try {
        const data = await favoritehMovies();
        console.log("Data Favorites:", data);
        setFavorites(data); 
      } catch (err) {
        console.log("Error:", err);
        setError(err.message); 
      } finally {
        setLoading(false); 
      }
    };
  
    loadFavorites();
  }, []);

  const handleFavoriteToggle = async (movieCode) => {
    if (!movieCode) {
      console.error("Invalid movie code:", movieCode);
      return;
    }

    console.log("Movie Code:", movieCode);
    try {
      // Cập nhật trạng thái yêu thích trong API
      await updateFavorite(movieCode);
      console.log("Favorite status updated successfully.");
      
      // Thay đổi trạng thái local để cập nhật icon
      setFavorites(prevFavorites =>
        prevFavorites.map(favorite =>
          favorite.movie_code === movieCode
            ? { ...favorite, isFavorite: !favorite.isFavorite } // đảo trạng thái yêu thích
            : favorite
        )
      );
    } catch (error) {
      console.error("Error in handleFavoriteToggle:", error);
    }
  };

  return (
    <View style={parentStyles.screenContainer}>
      <Header title="Profile" showBackButton={false} />
      <ScrollView style={styles.container} contentContainerStyle={styles.contentContainerStyle}>
        <View style={styles.profileIcon}>
          <Image source={require('@/src/assets/images/avatar.png')} style={styles.avatarIcon} />
          <View style={styles.profileDetails}>
            <Text style={styles.profileText}>@username</Text>
            <Text style={styles.profileText}>Chinh sua thong tin</Text>
          </View>
        </View>
        <ScrollView style={styles.profileContainer}>
          <View style={styles.title}>
            <Text style={[{ fontSize: 20, fontWeight: 'bold', color: COLORS.Black, marginLeft: 10 }]}>
              Film yêu thích
            </Text>
            <Text style={styles.textWithBorder} />
          </View>
          <View style={styles.filmList}>
            {favorites.map((favorite) => (
              <TouchableOpacity
                key={favorite.id || favorite.movie_name}
                style={styles.filmItem}
                onPress={() => {
                  router.push({
                    pathname: '/DetailAboutScreen',
                    params: {
                      movieCode: favorite.movie_code,
                      id: favorite.id,
                      movieName: favorite.movie_name,
                      movieGenre: favorite.movie_genre,
                      imageUrl: favorite.image_url,
                    },
                  });
                }}
              >
                <Image source={{ uri: favorite.image_url }} style={styles.movieImg} />
                <View style={styles.profileDetailsList}>
                  <Text style={styles.profileText} numberOfLines={2} ellipsizeMode="tail">
                    {favorite.movie_name}
                  </Text>
                  <Text style={styles.profileType}>
                    {favorite.type ? 'Series' : 'Movie'}
                  </Text>
                  <Text style={[styles.iconContainer, { marginBottom: 0 }]}>
                    <Ionicons name="calendar" size={18} color="#9a9a9d" />
                    <View style={styles.iconTextContainer}>
                      <Text style={styles.iconText}>{new Date(favorite.release_date).getFullYear()}</Text>
                    </View>
                    <Ionicons name="timer-sharp" size={18} color="#9a9a9d" style={{ marginLeft: 10 }} />
                    <View style={styles.iconTextContainer}>
                      <Text style={styles.iconText}>{new Date(favorite.release_date).getFullYear()}</Text>
                    </View>
                  </Text>
                </View>
                <TouchableOpacity
                  style={styles.iconButton}
                  onPress={() => handleFavoriteToggle(favorite.movie_code)}
                >
                  <AntDesign name={favorite.isFavorite ? "hearto" : "heart"} size={24} color={favorite.isFavorite ? "#9a9a9d" : "red"} />
                </TouchableOpacity>
              </TouchableOpacity>
            ))}
          </View>
        </ScrollView>
      </ScrollView>
    </View>
  );
};

export default Profile;

const parentStyles = StyleSheet.create({
  screenContainer: {
    height: 100,
    flex: 1,
    justifyContent: 'space-between',
    flexDirection: 'column',
  },
});

const styles = StyleSheet.create({
  container: {
    backgroundColor: COLORS.White,
    height: "100%",
  },
  contentContainerStyle: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },
  profileIcon: {
    flexDirection: 'row',
    alignItems: 'center',
    marginHorizontal: 40,
    marginTop: 30,
  },
  profileDetailsList: {
    flexDirection: 'column',
    alignItems: 'flex-start',
    marginLeft: 10,
    height: 130,
    justifyContent: 'space-evenly',
    alignItems: 'center',
  },
  profileDetails: {
    flexDirection: 'column',
    alignItems: 'flex-start',
    marginLeft: 10,
    justifyContent: 'space-evenly',
    alignItems: 'center',
  },
  profileText: {
    fontSize: 16,
    color: '#333333',
    marginBottom: 5,
    flexWrap: 'wrap', 
    width: 200,
    textAlign: 'left',
    justifyContent: 'center',
    fontWeight: 'bold',
    top: 0,
  },
  iconContainer: {
    fontSize: 12,
    width: 200,
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
  },
  profileType: {
    fontSize: 16,
    color: '#333333',
    marginBottom: 5,
    flexWrap: 'wrap', 
    width: 200,
    textAlign: 'left',
    justifyContent: 'center',
  },
  avatarIcon: {
    width: 60,  
    height: 60,
    color: COLORS.Blue,
    borderRadius: 40,
    borderWidth: 1,    
    borderColor: '#FFFFFF',
  }, 
  profileContainer: {
    marginHorizontal: 20,
    marginVertical: 20,
  },
  title: {
    alignItems: 'flex-start',
    justifyContent: 'center',
  },
  textWithBorder: {
    width: '40%',
    borderBottomWidth: 3.5,
    borderColor: COLORS.Blue,
    borderRadius: 1.5,
  },
  filmList: {
    flexDirection: 'column',
  },
  filmItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  movieImg: {
    width: 120,  
    height: 120,
    color: COLORS.Blue,
    borderRadius: 10,
    borderWidth: 1,    
    borderColor: '#FFFFFF',
  },
  iconTextContainer: {
    paddingHorizontal: 5,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  iconText: {
    fontSize: 12,
    color: '#9a9a9d',
    justifyContent: 'center',
    textAlign: 'center',
  },
});
