import React,{ useState, useRef,useEffect  } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  TouchableOpacity, 
  Image , 
  ScrollView,
  Dimensions,
  ActivityIndicator  } from 'react-native'; 
import { COLORS, SPACING } from '@/src/theme/theme';
import { useRouter } from 'expo-router';
import { fetchMoviesByType ,fetchMoviesByHot,fetchMovies,fetchBanner,fetchCategories} from '@/src/api/api';
import MovieSection from '@/app/components/MovieSection';
// import mockMovies from '@/app/mockMovies';
import Header from '@/app/components/Header';
import Footer from '@/app/components/Footer';





const { width: screenWidth } = Dimensions.get('window');

const Home = () => {
  const [currentIndex, setCurrentIndex] = useState(0); 
  const scrollRef = useRef(null); 
  const router = useRouter(); 


  const [movies, setMovies] = useState([]); 
  const [moviesTypeTrue, setMoviesTypeTrue] = useState([]); 
  const [moviesTypeFalse, setMoviesTypeFalse] = useState([]); 
  const [moviesTypeHot, setMoviesTypeHot] = useState([]); 
  const [moviesBanner, setMoviesBanner] = useState([]); 
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null); 
  const [activeCategory, setActiveCategory] = useState(null);


  const handleScroll = (event) => {
    const offsetX = event.nativeEvent.contentOffset.x;
    const index = Math.round(offsetX / screenWidth);
    setCurrentIndex(index);
  };

  const handleDotPress = (index) => {
    // if (scrollRef.current) {
    //   scrollRef.current.scrollTo({
    //     x: index * screenWidth,
    //     animated: true,
    //   });
    //   setCurrentIndex(index);
    // }
  };




  useEffect(() => {
    const loadMovies = async () => {
      try {
        const moviesDataTypeTrue = await fetchMoviesByType(true); 
        const moviesDataTypeFalse = await fetchMoviesByType(false); 
        const moviesTypeHot = await fetchMoviesByHot();
        const moviesBanner = await fetchBanner();
        const data = await fetchMovies(); 
        setMoviesTypeTrue(moviesDataTypeTrue); 
        setMoviesTypeFalse(moviesDataTypeFalse); 
        setMoviesTypeHot(moviesTypeHot);
        setMoviesBanner(moviesBanner);
        setMovies(data); 
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false); 
      }
    };

    loadMovies();
  }, []);



  useEffect(() => {
    const loadCategories = async () => {
      try {
        const categories = await fetchCategories(); 
        setCategories(categories); 
        console.log("category =>>>",categories);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false); 
      }
    };

    loadCategories();
  }, []);






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
    <View style={parentStyles.screenContainer}>
      <Header title="Movies" showBackButton={false} />
      <ScrollView  style={styles.container} 
      contentContainerStyle={styles.contentContainerStyle}>
        <View style={styles.contentContainer}>
          <ScrollView 
            ref={scrollRef}
            horizontal
            showsHorizontalScrollIndicator={false}
            pagingEnabled
            onScroll={handleScroll}
            scrollEventThrottle={16}
            contentContainerStyle={styles.scrollContent}
            snapToInterval={screenWidth}
            snapToAlignment="center" 
            decelerationRate="fast" 
          >
              {moviesBanner.slice(0,4).map((movie) => (
                <View key={movie.id} style={styles.card}>
                  <Image source={{ uri: movie.banner_image }} style={styles.image} />
                </View>
              ))}
          </ScrollView>  
          <View style={styles.pagination}>
            {movies.slice(0,4).map((_,index) => (
                <TouchableOpacity
                  key={index}
                  style={[
                    styles.paginationDot,
                    currentIndex === index && styles.activePaginationDot, 
                  ]}
                  onPress={() => handleDotPress(index)}
                />
              ))}
          </View>
        <View>

        <Text style={styles.categoryTitle}>Category</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.categoryContainer}>
          {categories.map((category,index) => (
            <TouchableOpacity 
              key={index}
              style={[
                styles.category,
                activeCategory === category && styles.activeCategory,
            ]}
              onPressIn={() => setActiveCategory(category)} 
              onPressOut={() => setActiveCategory(null)} 
              onPress={() => {
                router.push({
                  pathname: '/Category',
                  params: {
                    categoryName: category.name,
                    categoryCode: category.category_code
                  },
                });
                console.log('Category Code=>>>>>>', category.name)
              }} 
              >
              <Text  style={[
                styles.categoryText,
                activeCategory === category && styles.activeCategoryText, 
              ]}>{category.name}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
        </View>
          <View>
          <MovieSection
              title="Phim lẻ"
              movies={moviesTypeFalse}
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
              isSingle={true}
            />
          </View>

          <View>
          <MovieSection
            title="Phim bộ"
            movies={moviesTypeTrue}
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
          </View>
          <View style={{ marginBottom: 50 }}>
          <MovieSection
            title="Hot"
            movies={moviesTypeHot}
            onPress={(movie) => {
              console.log('Hot:', movie);
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
          </View>
        </View>
      </ScrollView>
      <Footer style ={styles.footer} />
  </View>
);
};

export default Home;

const parentStyles = StyleSheet.create({
  screenContainer: {
    flex: 1, 
    justifyContent: 'space-between', 
    flexDirection: 'column',
  },
});

const styles = StyleSheet.create({


container: {
  backgroundColor: COLORS.White,
  flex1: 1,
},

contentContainerStyle: {
  flexDirection: 'column',
  justifyContent: 'flex-start',
},







title: {
  paddingLeft: 50,
  fontSize: 18,
  fontWeight: 'bold',
  color: COLORS.Blue,
  textAlign: 'center',
  marginTop: 20,
},


iconContainer:{
  flexDirection: 'row',  
  alignItems: 'center',  
  marginLeft: 10,
  marginTop: 20,
},


  searchIcon: {
    width: 30,  
    height: 30, 
    color: COLORS.Blue,
    marginRight: SPACING.space_10,
  },

  avatarIcon: {
    width: 40,  
    height: 40, 
    color: COLORS.Blue,
    borderRadius: 20,
    borderWidth: 1,    
    borderColor: '#FFFFFF',
  }, 
  avatarContainer: {
    position: 'relative'
  },

  crossLines: {
    position: 'absolute',
    top: 0,  
    left: 0,  
    width: '150%',
    height: '150%',
    zIndex: -1,
  },

  contentContainer:{
    backgroundColor: '#fff',
    paddingVertical: 20,
    width: '100%',  
  },

  categoryTitle:{
    paddingHorizontal: 20,
    marginLeft: 0,
    marginTop: 30,
    fontSize: 16,
    fontWeight: 'bold',
    color: COLORS.Black,
  },

  categoryContainer: {
    flexDirection: 'row', 
    paddingHorizontal: 14, 
    marginVertical: 16, 
  },
  category: {
    borderRadius: 16, 
    paddingVertical: 8, 
    paddingHorizontal: 14.5, 
    marginHorizontal: 8, 
    backgroundColor: '#dfe9ff', 
  },
  categoryText: {
    fontSize: 14,
    fontWeight: 'semibold',
    color: "#1c81c4",
  },


  movieTitle:{
    paddingHorizontal: 20,
    marginLeft: 0,
    marginTop: 10,
    fontSize: 16,
    fontWeight: 'bold',
    color: COLORS.Black,
  },

    movieName: {
      fontSize: 14,
      fontWeight: 'bold',
      color: COLORS.Black,
    },
    movieGenre: {
      fontSize: 12,
      color: COLORS.Grey,
      marginTop: 4,
    },


    scrollContent: {
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
    },

    card: {
      width: screenWidth - 40,
      height: 150, 
      marginHorizontal: 20,
      borderRadius: 10,
      overflow: 'hidden',
    },
    image: {
      width: '100%',
      height: '100%',
      resizeMode: 'cover',
      borderRadius: 8,
    },

    pagination: {
      flexDirection: 'row',
      justifyContent: 'center',
      marginTop: 10,
    },

    paginationDot: {
      width: 8,
      height: 8,
      borderRadius: 4,
      backgroundColor: '#f2f2f7',
      marginHorizontal: 5,
    },

    activePaginationDot: {
      width: 30, 
      height: 8,
      borderRadius: 25, 
      backgroundColor: '#0072bc', 
    },

    cardWrapper: {
      flexDirection: 'row',
      justifyContent: 'center',  // Căn giữa tất cả các card
      alignItems: 'center',
    },
    
    activeCategory: {
      backgroundColor: '#0072bb', 
    },
    activeCategoryText: {
      color: '#ebe710', 
    },
});