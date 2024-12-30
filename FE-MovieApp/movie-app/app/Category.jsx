import React,{ useState,useEffect  } from 'react';
import { 
  View, 
  Text, 
  StyleSheet,
  Dimensions, 
  ScrollView,
  ActivityIndicator
} from 'react-native';
import Header from '@/app/components/Header';
import { COLORS} from '@/src/theme/theme';
import MovieGrid from '@/app/components/MovieGrid';
import { useRouter } from 'expo-router';
import {fetchMoviesByCategory} from '@/src/api/api';
import { useLocalSearchParams } from 'expo-router';



const { width: screenWidth } = Dimensions.get('window');

const Category = () => {

  const {categoryName, categoryCode} = useLocalSearchParams();
  console.log('Category Codehahahahaha: ', categoryName);
  console.log("Categody_code:",categoryCode)
  const router = useRouter(); 
  const [movies, setMovies] = useState([]); 
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null);

  const navigateToBack = () => {
    router.back(); 
  };


  useEffect(() => {
    const loadMovies = async () => {
      setLoading(true); // Set loading true trước khi bắt đầu tải dữ liệu
      try {
        const data = await fetchMoviesByCategory(categoryName); // Sử dụng categoryName
        console.log("Data:", data);
        setMovies(data); 
      } catch (err) {
        console.log("Error:", err);
        setError(err.message); // Xử lý lỗi
      } finally {
        setLoading(false); // Set loading false sau khi kết thúc
      }
    };
  
    loadMovies();
  }, [categoryName]); 

    
  
    if (loading) {
      return (
        <View style={styles.loaderContainer}>
          <ActivityIndicator size="large" color={COLORS.Blue} />
        </View>
      );
    }
  
    if (error) {
      return (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>Error: {error}</Text>
        </View>
      );
    }
  
  return (
    <ScrollView  style={styles.container} 
        contentContainerStyle={styles.contentContainerStyle}>
      <Header title={categoryName} showBackButton={true} onBack={navigateToBack} />
      <MovieGrid 
        movies={movies}
        onPress={(movie) => {
          router.push({
            pathname: '/DetailAboutScreen',
            params: {
              movieCode: movie.movie_code,
              id: movie.id,
              movieName: movie.movie_name,
              movieGenre: movie.movie_genre,
              imageUrl: movie.image_url,
            },
          });
        }} 
      />
    </ScrollView>
  )
}

export default Category

const styles = StyleSheet.create({
  container: {
    backgroundColor: COLORS.White,
    height:"100%",
  },
  
  contentContainerStyle: {
    flexDirection: 'column',
    alignItems: 'flex-start',
    justifyContent: 'flex-start',
  }

})