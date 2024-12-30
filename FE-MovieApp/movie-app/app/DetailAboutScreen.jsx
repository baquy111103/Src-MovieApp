import { useState,useEffect } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  TouchableOpacity, 
  Image, 
  ScrollView,
  Dimensions,
  ActivityIndicator  
} from 'react-native'; 
import { useLocalSearchParams } from 'expo-router';
import { COLORS, SPACING } from '@/src/theme/theme';
import { useRouter } from 'expo-router';
import { fetchMoviesEpisodes,fetchMovieDetails,favoritehMovies, addFavoritoes, updateFavorite } from '@/src/api/api';
import AntDesign from 'react-native-vector-icons/AntDesign';
import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';
import Ionicons from 'react-native-vector-icons/Ionicons';
// import mockMovies from '@/app/mockMovies';


  const { width,height } = Dimensions.get('window');

  const DetailAboutScreen = () => {
    const router = useRouter();
    const { movieCode } = useLocalSearchParams(); 
    const [movieDetails, setMovieDetails] = useState(null);
    const [movieEpisodes, setMovieEpisodes] = useState([]);
    const [allFavorites,setAllFavorites] = useState([]);
    const [loading, setLoading] = useState(true); 
    const [error, setError] = useState(null); 
    const [isExpanded, setIsExpanded] = useState(false); 
    const [activeTab, setActiveTab] = useState('about');
    const [isFavorite, setIsFavorite] = useState(false);



    const navigateToBack = () => {
      router.back(); 
    };


    const MAX_LENGTH = 250; 

    



    useEffect(() => {
      const getMovieEpisodes = async () => {
        setLoading(true); 
        try {
          const episodes = await fetchMoviesEpisodes(movieCode); 
          if (Array.isArray(episodes)) {
            const sortedEpisodes = episodes.sort((a, b) => a.episode_number - b.episode_number);
            setMovieEpisodes(sortedEpisodes);
          } else {
            setMovieEpisodes([]); // Nếu không phải mảng, khởi tạo là mảng rỗng
          }
          const details = await fetchMovieDetails(movieCode);
          setMovieDetails(details); 
        } catch (err) {
          setError('Failed to load movie episodes');
          setMovieEpisodes([]); // Đảm bảo mảng episodes luôn có giá trị hợp lệ
        } finally {
          setLoading(false); 
        }
      };
      getMovieEpisodes();
    }, [movieCode]);

    useEffect(() => {
      const loadFavorites = async () => {
        setLoading(true); // Set loading true trước khi bắt đầu tải dữ liệu
        try {
          const data = await favoritehMovies(); // Sử dụng categoryName
          console.log("Data Favorites=>>>>>>>>>>>>>>>>>>>>>.:", data);
          setAllFavorites(data); 
        } catch (err) {
          console.log("Error:", err);
          setError(err.message); // Xử lý lỗi
        } finally {
          setLoading(false); // Set loading false sau khi kết thúc
        }
      };
    
    loadFavorites();
    }, [])

    useEffect(() => {
      if (allFavorites && allFavorites.length > 0) {
        const favoriteMovie = allFavorites.find(fav => fav.movie_code === movieCode);
        if (favoriteMovie) {
          setIsFavorite(true); // Nếu có, đổi màu thành đỏ
        } else {
          setIsFavorite(false); // Nếu không, giữ nguyên màu trắng
        }
      }
    }, [allFavorites, movieCode]);

    const toggleFavorite = async () => {
      try {
        if (isFavorite) {
          // Nếu là yêu thích, gọi API để cập nhật yêu thích
          await updateFavorite(movieCode);
        } else {
          // Nếu không phải yêu thích, gọi API để thêm vào yêu thích
          await addFavoritoes(movieCode);
        }
        
        // Đổi trạng thái yêu thích
        setIsFavorite(!isFavorite); 
      } catch (error) {
        console.error('Error updating favorite:', error);
      }
    };
    
    if (loading) {
      return (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={COLORS.primary} />
        </View>
      );
    }
  
    if (error) {
      return (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>{error}</Text>
        </View>
      );
    }
  
    if (!movieDetails) {
      console.log('No movie details available');
      return null; 
    }
  
    const { episode_number } = movieEpisodes;
    const { movie_name, movie_genre, image_url, release_date, censorship, language, description,duration,movieActors ,video_url } = movieDetails || {};
    const releaseYear = new Date(release_date).getFullYear();
    const truncatedDescription = description.length > MAX_LENGTH ? description.slice(0, MAX_LENGTH) + '... more' : description


    const getEpisodeCount = () => {
      return movieEpisodes.length; 
    };



    return (
      <ScrollView style={styles.container}>
        <View style={styles.imageContainer}>
          <Image source={{ uri: image_url }} style={styles.coverImage} />
          <View style={styles.overlayButtons}>
            <TouchableOpacity 
            style={styles.iconButton}
            onPress={navigateToBack}
            >
              <AntDesign 
                name="arrowleft"
                size={24}
                color={COLORS.White}
              />
            </TouchableOpacity>
            <View style={styles.rightButtons}>
           
            <TouchableOpacity 
              style={styles.iconButton}
              onPress={toggleFavorite} // Thêm hành động khi nhấn vào icon yêu thích
            >
              <AntDesign 
                name={isFavorite ? "heart" : "hearto"} // Thay đổi icon khi yêu thích
                size={24}
                color={isFavorite ? "red" : COLORS.White} // Đổi màu khi yêu thích
              />
            </TouchableOpacity>
              <TouchableOpacity 
              style={styles.iconButton}
              >
                <AntDesign 
                  name="sharealt"
                  size={24}
                  color={COLORS.White}
                />
              </TouchableOpacity>
            </View>
          </View>
          
        </View>
        <View>
          <View style={styles.title}>
            <Text style={styles.movieTitle}
            numberOfLines={1}
            ellipsizeMode="tail"
            >{movie_name}</Text>
            <View style={styles.playButton}>
              <TouchableOpacity 
              style={styles.iconPlay}
              onPress={(movie) => {
                console.log('Hot:', movie);
                router.push({
                  pathname: '/MoviesDetailScreen',
                  params: {
                    movieName: movie_name,
                    episodeNumber: 1,
                    movieCode: movieCode, 
                    videoUrl: video_url,
                    description: description,
                  },
                });
              }}
              >
                <FontAwesome5
                  name="play"
                  size={24}
                  color="#f5f008"
                />
              </TouchableOpacity>
            </View>
          </View>
          <View style={styles.timeContainer}>
              <Ionicons
                name='calendar'
                size={18}
                color="#9a9a9d"
              />
              <Text style={{color: '#9a9a9d', marginLeft: 5}}>{releaseYear}</Text>
              <Ionicons
                name='timer-sharp'
                size={18}
                color="#9a9a9d"
                style={{marginLeft: 10}}
              />
              <Text style={{color: '#9a9a9d', marginLeft: 5}}>
                {getEpisodeCount() >= 2 ? `${getEpisodeCount()} Episodes` : `${duration} Minutes`}
              </Text>
              {/* <Ionicons
                name='heart-outline'
                size={18}
                color="#9a9a9d"
                style={{marginLeft: 10}}
              />
              <Text style={{color: '#9a9a9d', marginLeft: 5}}>{duration} Love</Text> */}
          </View>
        </View>

        <View style={styles.tabsContainer}>
          <TouchableOpacity
            style={[styles.tab, activeTab === 'episodes' && styles.activeTab]}
            onPress={() => setActiveTab('episodes')}
          >
            <Text style={[styles.tabText, activeTab === 'episodes' && styles.activeTabText]}>Episodes</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.tab, activeTab === 'about' && styles.activeTab]}
            onPress={() => setActiveTab('about')}
          >
            <Text style={[styles.tabText, activeTab === 'about' && styles.activeTabText]}>About</Text>
          </TouchableOpacity>


        </View>
        {activeTab === 'about' ? (
        <View style={styles.detailsContainer}>
          {/* Movie Genre */}
          <View style={styles.detailRow}>
            <Text style={styles.movieAttributeTitle}>Movie genre:</Text>
            <Text style={styles.movieAttribute}>{movie_genre}</Text>
          </View>

    {/* Censorship */}
          <View style={styles.detailRow}>
            <Text style={styles.movieAttributeTitle}>Censorship:</Text>
            <Text style={styles.movieAttribute}>{censorship}+</Text>
          </View>

    {/* Language */}
          <View style={styles.detailRow}>
            <Text style={styles.movieAttributeTitle}>Language:</Text>
            <Text style={styles.movieAttribute}>{language}</Text>
          </View>
          <Text style={styles.movieStory}>Story Line: </Text>
          <Text style={styles.movieDescription}>
            {truncatedDescription}
          </Text>
 
          <Text style={styles.movieStory}>Cast and Crew</Text>
          <ScrollView horizontal  showsHorizontalScrollIndicator={false} style={styles.castContainer}>
              {movieActors && movieActors.length > 0 ? (
                movieActors.map((actor, index) => (
                  <View key={index} style={styles.actorContainer}>
                    <Image source={{ uri: actor.avatar }} style={styles.actorAvatar} />
                    <View>
                      <Text style={styles.actorName}
                      numberOfLines={1} 
                      ellipsizeMode="tail"
                      >
                        {actor.actor_name}
                      </Text>
                      <Text style={{color: '#9a9a9d'}}>
                        {actor.status ? 'Directors' : 'Actors'}
                      </Text>
                    </View>
                  </View>
                ))
              ) : (
                <Text>No cast and crew information available.</Text>
              )}
          </ScrollView>
        </View>

        ) : (
          <View style={styles.episodesContainer}>
              {movieEpisodes && movieEpisodes.length > 0 ? (
                movieEpisodes.map((episode, index) => (
                  <TouchableOpacity 
                  key={index} 
                  style={styles.episodeItem}
                  onPress={()=>
                    router.push({
                      pathname: '/MoviesDetailScreen',
                      params: {
                        movieName: episode.movie_name,
                        episodeNumber: episode.episode_number,
                        movieCode: movieCode, 
                      },
                    })
                  }
                  >
                    <Text style={styles.episodesText}>
                      {episode.episode_number}
                    </Text>
                  </TouchableOpacity>
                ))
              ) : (
                <Text>No episodes available.</Text>
              )}
          </View>
        )}
      </ScrollView>
    );
  };

  export default DetailAboutScreen;



  const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
    },
    imageContainer: {
      width: width,
      height: width * 0.56,
      
    },
    coverImage: {
      width: '100%',
      height: '100%',
      resizeMode: 'cover', 
    },

    overlayButtons: {
      position: 'absolute',
      marginTop: SPACING.space_62,
      left: SPACING.space_24,
      right: SPACING.space_24,
      flexDirection: 'row',
      justifyContent: 'space-between',
    },

    iconButton: {
      padding: 8,
      backgroundColor: 'rgba(0, 0, 0, 0.4)',
      borderRadius: 50,
    },

    coverImage: {
      width: width,
      height: width * 0.56,
      resizeMode: 'cover',
      marginBottom: 20,
    },


    rightButtons: {
      flexDirection: 'row',
      gap: 8,
    },
    
    detailsContainer: {
      padding: 16,
    },


    detailRow: {
      flexDirection: 'row',  
    },


    movieAttributeTitle: {
      fontSize: 16,
      fontWeight: 'semibold',
      color: "#9a9a9d",
      width: 120, 
    },

    movieAttribute: {
      fontSize: 16,
      color: COLORS.Black,
      marginBottom: 16,
      fontWeight: 'Semibold',
      marginLeft: SPACING.space_32,
    },


    title:{
      marginVertical: 20,
      paddingHorizontal: 16,
    },
    

    movieTitle: {
      fontSize: 24,
      fontWeight: 'bold',
      color: '#333',
      marginBottom: 16,
      maxWidth: '70%',
    },


    movieStory:{
      fontSize: 20,
      fontWeight: 'bold',
      color: '#333',
      marginBottom: 20,
      margiTop: 30,
    },


    tabsContainer: {
      flexDirection: 'row',
      justifyContent: 'space-around',
      backgroundColor: '#ffffff',
      paddingVertical: 10,
      position: 'relative',
    },


    tab: {
      paddingVertical: 8,
      paddingHorizontal: 16,
    },
    activeTab: {
      borderBottomWidth: 3,
      borderBottomColor: '#007bff',
      width: width / 2.5,
      alignItems: 'center',
      borderRadius: 0.5,
    },
    tabText: {
      fontSize: 16,
      color: '#555',
    },
    activeTabText: {
      color: '#007bff',
      fontWeight: 'bold',
    },


    title: {
      marginVertical: 20,
      paddingHorizontal: 16,
      justifyContent: 'space-between',
      flexDirection: 'row',
    },

    iconPlay:{
      width: 45,
      height: 45,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: "#0072bc",
      borderRadius: 60,
      paddingLeft: 5,
    },

    timeContainer: {
      marginTop: -25,
      marginBottom: 10,
      paddingHorizontal: 16,
      flexDirection: 'row',
      alignItems: 'center',
      color: '#9a9a9d',
    },

    movieDescription: {
      fontSize: 16,
      color: '#555',
      marginBottom: 16,
    },

    episodesContainer: {
      flexDirection: 'row', 
      flexWrap: 'wrap', 
      justifyContent: 'flex-start', 
      padding: 16,
    },

    episodeItem: {
      width: width * 0.2,
      height: width * 0.15,
      justifyContent: 'center',
      alignItems: 'center',
      marginRight: 10,
      
    },

    episodesText: {
      fontSize: 16,
      paddingTop: height * 0.012,
      width: '100%',
      color: '#555',
      textAlign: 'center',
      justifyContent: 'center',
      marginBottom: height * 0.02,
      height: '70%',
      borderRadius: 30,
      backgroundColor: '#f5f5f5',
    },


    castContainer: {
      flexDirection: 'row',
      // marginBottom: 16,
      // paddingHorizontal: 16,
    },



    actorContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      marginBottom: 16,
      marginRight: 30,
    },

    actorAvatar: {
      width: 50,
      height: 50,
      borderRadius: 25,
      marginRight: 10,
    },
    actorName: {
      fontSize: 16,
      fontWeight: 'semibold',
      color: '#333',
      maxWidth: 120,
      paddingBottom: 8,
    },
  });



