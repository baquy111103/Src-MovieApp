import React, { useEffect, useState, useRef } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  TouchableOpacity, 
  ScrollView, 
  FlatList } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useRouter } from 'expo-router';
import { COLORS, SPACING } from '@/src/theme/theme';
import { Video } from 'expo-av'; 
import { useLocalSearchParams } from 'expo-router';
import { fetchMoviesEpisodes, fetchMoviesByHot } from '@/src/api/api';
import Header from '@/app/components/Header';
import MovieSection from './components/MovieSection';

const MoviesDetailScreen = () => {
  const router = useRouter();
  const { movieCode, episodeNumber, movieName, videoUrl ,description} = useLocalSearchParams(); 
  const [episodes, setEpisodes] = useState([]);
  const [moviesTypeHot, setMoviesTypeHot] = useState([]); 
  const [currentEpisode, setCurrentEpisode] = useState(null);
  const [videoPosition, setVideoPosition] = useState(0); 
  const videoRef = useRef(null); 
  const MAX_LENGTH = 250; 

  // Fetch các tập phim khi movieCode hoặc episodeNumber thay đổi
  useEffect(() => {
    const fetchEpisodes = async () => {
      try {
        const data = await fetchMoviesEpisodes(movieCode); 
        setEpisodes(data);
        const selectedEpisode = data.find(
          (episode) => episode.episode_number === parseInt(episodeNumber, 10)
        );
        setCurrentEpisode(selectedEpisode); 
      } catch (error) {
        console.error('Lỗi khi gọi API:', error);
      }
    };

    fetchEpisodes();
  }, [movieCode, episodeNumber]);


  useEffect(() => {
    const fetchMoviesTypeHot = async () => {
      try {
        const data = await fetchMoviesByHot(); 
        setMoviesTypeHot(data);
      } catch (error) {
        console.error('Lỗi khi gọi API:', error);
      }
    };

    fetchMoviesTypeHot();
  }, []);

  useEffect(() => {
    const saveVideoPosition = async () => {
      if (videoRef.current && currentEpisode) {
        const currentTime = await videoRef.current.getStatusAsync();
        const key = `videoPosition_${movieCode}_${currentEpisode.episode_number}`;
        await AsyncStorage.setItem(key, currentTime.position.toString());
      }
    };

    saveVideoPosition();
  }, [currentEpisode]);

  useEffect(() => {
    const loadVideoPosition = async () => {
      if (currentEpisode) {
        const key = `videoPosition_${movieCode}_${currentEpisode.episode_number}`;
        const savedPosition = await AsyncStorage.getItem(key);
        if (savedPosition) {
          setVideoPosition(parseFloat(savedPosition));
        }
      }
    };

    loadVideoPosition();
  }, [currentEpisode]);

  useEffect(() => {
    setVideoPosition(0);  
  }, [currentEpisode]);

  // Lưu vị trí và dừng video khi rời khỏi màn hình
  useEffect(() => {
    return () => {
      if (videoRef.current) {
        videoRef.current.pauseAsync(); // Dừng video khi rời khỏi màn hình
      }
    };
  }, []);

  const handleMoviePress = (movie) => {
    console.log("Nhấn vào phim:", movie);
    if (videoRef.current) {
      videoRef.current.pauseAsync(); 
    }

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
  };


  const handleVideoFinish = () => {
    // Kiểm tra nếu có tập phim tiếp theo
    const nextEpisode = episodes.find(
      (episode) => episode.episode_number === currentEpisode.episode_number + 1
    );
    if (nextEpisode) {
      setCurrentEpisode(nextEpisode); 
    } else {
      if (videoRef.current) {
        videoRef.current.pauseAsync(); 
      }
    }
  };

  

  return (
    <View style={parentStyles.screenContainer}>
      <Header title="Play film" showBackButton={false} />
      <ScrollView style={styles.container} contentContainerStyle={styles.contentContainerStyle}>
        {/* Video Player */}
        <View style={styles.videoContainer}>
          {currentEpisode ? (
            <Video
              ref={videoRef}
              style={styles.video}
              source={{ uri: currentEpisode.video_url }}
              useNativeControls
              resizeMode="contain"
              shouldPlay={true}    
              // isLooping = {false}
              volume={1}
              isMuted={false}
              positionMillis={videoPosition}
              onPlaybackStatusUpdate={(status) => {
                if (status.isPlaying) {
                  setVideoPosition(status.position);
                }
                if (status.didJustFinish) {
                  handleVideoFinish(); 
                }
              }}
            />
            
          ) : (
            <Video
              ref={videoRef}
              style={styles.video}
              source={{ uri: videoUrl }}
              useNativeControls
              resizeMode="contain"
              shouldPlay={true}  // Đảm bảo video không tự động chơi khi chuyển tập
              isLooping
              volume={1}
              isMuted={false}
              positionMillis={videoPosition}
              onPlaybackStatusUpdate={(status) => {
                if (status.isPlaying) {
                  setVideoPosition(status.position);
                }
              }}
            />
          )}
        </View>

        <View style={styles.descriptionContainer}>
          <Text style={styles.movieTitle}>{movieName}</Text>
          <Text style={styles.movieStory}> {description || currentEpisode?.description}</Text>
          {
            currentEpisode ? (
              <Text style={styles.movieEP}>Chọn tập</Text>
            ) : null
          }

          <FlatList
            data={episodes}
            keyExtractor={(item) => item.episode_number.toString()}
            renderItem={({ item }) => (
              <TouchableOpacity
                style={[styles.episodeItem, item.episode_number === currentEpisode?.episode_number && styles.selectedEpisode]}
                onPress={() => setCurrentEpisode(item)}>
                <Text
                  style={[styles.episodeText, item.episode_number === currentEpisode?.episode_number
                    ? { color: COLORS.Yellow }
                    : { color: COLORS.Black }]} >
                  {item.episode_number}
                </Text>
              </TouchableOpacity>
            )}
            horizontal
          />
          <View style={styles.sectionContainer}>
            <Text style={styles.sectionTitle}>Film hot</Text>
            <MovieSection
              movies={moviesTypeHot}
              onPress={handleMoviePress} // Gọi handleMoviePress khi nhấn vào phim
              style={styles.sectionList}
            />
          </View>
        </View>
      </ScrollView>
    </View>
  );
};

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
    height: '100%',
  },
  contentContainerStyle: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },

  videoContainer: {
    width: '100%',
    height: 200,
    backgroundColor: COLORS.Black,
  },
  video: {
    width: '100%',
    height: '100%',
    resizeMode: 'cover',
  },
  loadingText: {
    textAlign: 'center',
    color: COLORS.Black,
    marginTop: 20,
  },

  episodeItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
    width: 60,
    padding: SPACING.space_8,
    backgroundColor: '#edeeef',
    borderRadius: 20,
    marginRight: SPACING.space_20,
  },

  selectedEpisode: {
    backgroundColor: COLORS.Blue,
  },

  episodeText: {
    fontSize: 16,
  },

  descriptionContainer: {
    marginTop: SPACING.space_16,
    paddingHorizontal: SPACING.space_24,
  },

  movieTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.Black,
    marginVertical: SPACING.space_16,
  },
  movieEP: {
    fontSize: 20,
    fontWeight: 'bold',
    color: COLORS.Black,
    marginVertical: SPACING.space_24,
  },

  sectionContainer: {
    marginTop: SPACING.space_24,
    marginLeft: '-7%',
    marginRight: '-7%',
  },

  sectionTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.Black,
    marginLeft: SPACING.space_24,
    // backgroundColor: COLORS.Black,
  },



});

export default MoviesDetailScreen;
