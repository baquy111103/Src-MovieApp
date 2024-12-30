import React from 'react';
import { 
  View, 
  Text, 
  ScrollView, 
  TouchableOpacity, 
  Image, 
  StyleSheet 
} from 'react-native';
import { COLORS } from '@/src/theme/theme';

const MovieSection = ({ title, movies, onPress,isSingle  }) => {
  return (
    <View style={styles.sectionContainer}>
      <Text style={styles.sectionTitle}>{title}</Text>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.movieList}>
        {movies.map((movie) => (
          <TouchableOpacity
            key={movie.id} 
            style={[styles.movieItem, isSingle && styles.singleMovieItem]}
            onPress={() => onPress(movie)}>
            <Image 
              source={{ uri: movie.image_url }} 
              style={[styles.movieImage, isSingle && styles.singleMovieImage]}  />
            <View style={styles.movieInfo}>
              <Text style={styles.movieName} numberOfLines={1} ellipsizeMode="tail">{movie.movie_name}</Text>
              <Text style={styles.movieGenre} numberOfLines={1} ellipsizeMode="tail">{movie.movie_genre}</Text>
            </View>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginVertical: 10,
  },
  sectionTitle: {
    paddingHorizontal: 20,
    marginLeft: 0,
    marginTop: 10,
    fontSize: 16,
    fontWeight: 'bold',
    color: COLORS.Black,
  },
  movieList: {
    flexDirection: 'row',
    marginHorizontal: 20,
    marginTop: 10,
  },
  movieItem: {
    marginRight: 10,
    width: 120,
  },
  movieImage: {
    width: '100%',
    height: 150,
    resizeMode: 'cover',
    borderRadius: 8,
  },
  movieInfo: {
    padding: 8,
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

  singleMovieItem: {
    overflow: 'hidden',
    width: 200,
  },

  singleMovieImage: {
    width: '100%',
    height: 100,    
  },
});

export default MovieSection;
