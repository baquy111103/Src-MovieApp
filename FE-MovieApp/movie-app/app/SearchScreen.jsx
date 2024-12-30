import React, { useState,useCallback, useEffect } from 'react';
import {
   View, 
   Text, 
   StyleSheet, 
   TouchableOpacity,
   Image,
   TextInput, 
   ScrollView,
   Dimensions
  } from 'react-native';
import { useRouter } from 'expo-router';
import { Octicons } from '@expo/vector-icons';
import { COLORS, SPACING, BORDERRADIUS,FONTSIZE } from '@/src/theme/theme';
import { searchMovies ,fetchKeyword } from '@/src/api/api';
import Header from '@/app/components/Header';
import MovieGrid from '@/app/components/MovieGrid';
import debounce from 'lodash.debounce';


const { width,height } = Dimensions.get('window');


const SearchScreen = () => {
  const router = useRouter();
  const [keyword,setKeyword] = useState('');
  const [allKeyword,setAllKeyword] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');  
  const [searchResults, setSearchResults] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigateToBack = () => {
    router.back(); 
  };

    useEffect(() => {
      const fetchAllkeyword = async () => {
        try {
          const data = await fetchKeyword(); 
          setAllKeyword(data); 
          console.log("All keyword:", data); 
        } catch (err) {
          setError(err.message);
        } finally {
          setLoading(false); 
        }
      };
    
      fetchAllkeyword(); 
    }, []);
  

  const handleSearch = useCallback(
    debounce(async (query) => {
      const trimmedQuery = query.trim();
      if(keyword){
        setSearchResults(keyword);
        
      }
      if (!trimmedQuery) {
        setSearchResults([]); // Nếu không có từ khóa tìm kiếm thì xóa kết quả
        return;
      }

      setIsLoading(true); // Bật loading khi gọi API

      try {
        const results = await searchMovies(trimmedQuery); 
        setSearchResults(results);
      } catch (error) {
        console.error('Error searching movies:', error);
      } finally {
        setIsLoading(false); 
      }
    }, 100), 
    []
  );

  const handleTextChange = (text) => {
    setSearchQuery(text);
    handleSearch(text); 
  };

  const handleKeywordSelect = (selectedKeyword) => {
    setSearchQuery(selectedKeyword); // Cập nhật searchQuery với từ khóa đã chọn
    handleSearch(selectedKeyword); // Thực hiện tìm kiếm ngay lập tức
  };



  return (
   <ScrollView  style={styles.container} 
    contentContainerStyle={styles.contentContainerStyle}>
      <Header title="Search" showBackButton={true} onBack={navigateToBack} />
      <View style={styles.searchContainer}>
        <Image source={require('@/src/assets/images/blackSearch.png')} style={styles.searchIcon}/>
        <TextInput
          style={styles.searchInput}
          placeholder="Search..."
          placeholderTextColor={COLORS.Grey}
          value={searchQuery}
          onChangeText={handleTextChange}  
        />
      </View>

      {/* Hiển thị gợi ý tìm kiếm */}
      <View>
        {searchResults.length > 0 && (
          <MovieGrid
            movies={searchResults}
            isLoading={isLoading}
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
        )}

        {searchResults.length === 0 && searchQuery && (
          <View style={styles.noResultContainer}>
            <View style={styles.noResultIcon}>
              <Image 
                  source={require('@/src/assets/images/noresult.png')}  
                  style={styles.avatarIcon}
              />
            </View>
            <Text style={styles.noResultTitle}>Sorry, we can't find the movie :'(</Text>
            <Text style={styles.noResultDescription}>Không có dữ liệu, vui lòng {'\n'} quay lại sau</Text>
          </View>
        )}
      </View>

      {!searchQuery && (
     <>
      <View style={styles.rowContainer}>
        <Text style={styles.hotKeywords}>Hot keywords</Text>
        <TouchableOpacity style={styles.trashContainer}>
          <Octicons name="trash" marginTop={5} size={30} color='#5c5d61'/>
          <View style={[styles.horizontalBar, { top: 20,  width: 8, left: 7 }]} />
          <View style={[styles.horizontalBar, { bottom: 8,  width: 6 , left: 8}]} />
        </TouchableOpacity>
      </View>
      <View style={styles.keywordsContainer}>
          {allKeyword.map((keywordItem,index) => (
            <TouchableOpacity
            key={index}
            onPress={() => handleKeywordSelect(keywordItem.keyword)}
            >
              <Text style={styles.keywordItems}>{keywordItem.keyword}</Text>
            </TouchableOpacity>
          ))}
        </View>

     </>
      )}
    </ScrollView>
  );
};

export default SearchScreen;

const styles = StyleSheet.create({
  container: {
    backgroundColor: COLORS.White,
    height:"100%",
  },
  
  contentContainerStyle: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },
  searchContainer: {
    flexDirection: 'row', 
    alignItems: 'center', 
    marginTop: SPACING.space_32,
    // marginBottom: SPACING.space_20,
    width: '90%', 
    height: 50,
    backgroundColor: '#f5f5f5',
    borderRadius: BORDERRADIUS.space_10,
    paddingHorizontal: SPACING.space_10,
    marginHorizontal: SPACING.space_20,
  },
  searchInput: {
    width: '90%',
    height: 50,
    borderRadius: BORDERRADIUS.space_10,
    paddingHorizontal: SPACING.space_15,
    fontSize: FONTSIZE.size_16,
    fontWeight: 'semibold',
    color: '#333333',
    backgroundColor: '#f5f5f5',
  },

  searchIcon: {
    width: 30,  
    height: 30, 
    tintColor: 'black',
  },

  hotKeywords:{
    fontSize: FONTSIZE.size_18,
    fontWeight: 'bold',
    color: COLORS.Grey,
  },

  rowContainer: {
    marginTop: SPACING.space_24,
    marginHorizontal: SPACING.space_20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '90%', 
  },
  

  trashContainer: {
    position: 'relative', 
    width: 25, 
    height: 35,
  },
  
  horizontalBar: {
    position: 'absolute',
    height: 2, 
    backgroundColor: '#5c5d61', 
    borderRadius: 2,
  },


  
  noResultContainer: {
    marginTop: SPACING.space_36,
    alignItems: 'center',
    justifyContent: 'center',
  },
  noResultTitle: {
    fontSize: FONTSIZE.size_20,
    fontWeight: 'bold',
    color: COLORS.Grey,
  },
  noResultDescription: {
    fontSize: FONTSIZE.size_16,
    color: COLORS.Grey,
    marginTop: SPACING.space_8,
    textAlign: 'center',
  },


  noResultIcon: {
    width: 100,
    height: 100,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f5f5f5',
    borderRadius: 100,
  },

  avatarIcon: {
    width: '90%',
    height: '90%',
    resizeMode: 'contain',
  },

  keywordsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'flex-start',
    marginTop: SPACING.space_20,
    marginHorizontal: SPACING.space_20,
  },

  keywordItems: {
    fontSize: FONTSIZE.size_14,
    fontWeight: 'semibold',
    color: '#5c5d61',
    backgroundColor: '#eeeff0',
    paddingHorizontal: SPACING.space_16,
    paddingVertical: SPACING.space_8,
    borderRadius: 20,
    marginRight: SPACING.space_10,
    marginBottom: SPACING.space_10,
  },

});
