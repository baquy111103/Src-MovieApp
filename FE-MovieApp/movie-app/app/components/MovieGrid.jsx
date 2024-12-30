import { 
  View, 
  Text, 
  StyleSheet,
  Image,
  TouchableOpacity,
  Dimensions
} from 'react-native';
import { COLORS, SPACING } from '@/src/theme/theme';



const { width,height } = Dimensions.get('window');

const MovieSectionGrid = ({ movies,onPress }) => {
  return (
    <View style={styles.grid}>
      {movies.map((movie) => (
        <TouchableOpacity 
          key={movie.id} 
          style={styles.cardItem}
          onPress={() => onPress(movie)}
          >
          <Image source={{ uri: movie.image_url }} style={styles.cardImage} />
          <View style={styles.cardInfo}>
            <Text style={styles.cardName} numberOfLines={1} ellipsizeMode="tail">
              {movie.movie_name}
            </Text>
            <Text style={styles.cardGenre} numberOfLines={1} ellipsizeMode="tail">
              {movie.movie_genre}
            </Text>
          </View>
        </TouchableOpacity>
      ))}
    </View>
  );
};

export default MovieSectionGrid;



const styles = StyleSheet.create({
  grid: {
      flexDirection: 'row',
      flexWrap: 'wrap',
      justifyContent: 'flex-start',
      marginTop: SPACING.space_36,
      marginHorizontal: SPACING.space_12,
    },
  
    cardItem: {
      marginRight: width * 0.027,
      overflow: 'hidden',
      width: width * 0.285,
      marginBottom: width * 0.03,
    },
    cardImage: {
      width: '100%',
      height: height * 0.18,
      resizeMode: 'cover',
      borderRadius: 8,
    },
    cardInfo: {
      padding: height * 0.015,
    },
  
    cardName: {
      fontSize: 14,
      fontWeight: 'bold',
      color: COLORS.Black,
      overflow: 'hidden',
    },
    cardGenre: {
      fontSize: 12,
      color: COLORS.Grey,
      marginTop: height * 0.009,
    },
});