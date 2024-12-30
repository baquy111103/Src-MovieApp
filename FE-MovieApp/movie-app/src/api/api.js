import axios from 'axios';

const api = axios.create({
  baseURL: 'http://192.168.1.224:8080/api',
  timeout: 5000, 
  headers: {
    'Content-Type': 'application/json',
  },
});

export const fetchMovies = async () => {
  try {
    const response = await api.get('/movies'); 
    return response.data; 
  } catch (error) {
    console.error('Error fetching movies:', error);
    throw error; 
  }
};



export const fetchMovieDetails = async (code) => {
  try {
    const response = await api.get(`/movies/details/${code}`); 
    console.log('Movie Code:', code);
    // console.log('API Response Data:', response.data); 
    return response.data;
  } catch (error) {
    console.error('Error fetching movie details:', error);
    throw error;
  }
};


export const fetchMoviesEpisodes = async (code) => {
  try {
    const response = await api.get(`/movies/episodes?movie_code=${code}`); 
    return response.data; 
  } catch (error) {
    console.error('Error fetching movie episodes:', error);
    throw error; 
  }
};


export const fetchMoviesByType = async (type) => {
  try {
    const response = await api.get(`/movies/type?type=${type}`); 
    return response.data; 
  } catch (error) {
    console.error('Error fetching movies by type:', error);
    throw error; 
  }
};


export const fetchMoviesByHot = async () => {
  try {
    const response = await api.get(`/movies/hot`); 
    return response.data; 
  } catch (error) {
    console.error('Error fetching movies by hot:', error);
    throw error; 
  }
}


export const fetchBanner = async () => {
  try {
    const response = await api.get(`/banners/active`); 
    return response.data; 
  } catch (error) {
    console.error('Error fetching banner:', error);
    throw error; 
  }
}
export const searchMovies = async (query) => {
  try {
    if (!query.trim()) {
      return []; 
    }

    const response = await api.get(`/movies/search?movie_name=${query}`, {
      params: { query },
    });

    if (response.data.length === 0) {
      const response1 = await api.get(`/movies/search?movie_genre=${query}`, {
        params: { query },
      });

      if (response1.data.length === 0) {
        const response2 = await api.get(`/movies/search?actor_name=${query}`, {
          params: { query },
        });

        console.log('actor_name from API:', response2.data); 
        return response2.data; 
      } else {
        console.log('movie_genre from API:', response1.data); 
        return response1.data; 
      }
    } else {
      console.log('Movie from API:', response.data); 
      return response.data; 
    }
  
  } catch (error) {
    console.error('Error searching movies:', error);
    throw error;
  }
};






export const favoritehMovies = async () => {
  try {
    const response = await api.get(`/favorites`)
    return response.data; 
  } catch (error) {
    console.error('Error searching movies:', error);
    throw error;
  }
};




export const fetchKeyword = async () => {
  try {
    const response = await api.get('/keywords/active');

    return response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw error;
  }
};



// Category
export const fetchCategories = async () => {
  try {
    const response  = await api.get('/categories');
    return response.data.map((item) => ({
      name: item.name,
      category_code: item.category_code,  
    }));
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw error;
  }
};



export const fetchMoviesByCategory = async (categoryName) => {
  try {
    const response = await api.get(`/movies/category/${categoryName}`);
    console.log('Cresponse.data:', response.data); // In ra categoryName
    return response.data;
  } catch (error) {
    console.error('Error fetching movies by category:', error);
    throw error;
  }
};
export const updateFavorite = async (movieCode) => {
  try {
    console.log("Check Movie_code:", movieCode);
    await api.put(`/favorites/update?movie_code=${movieCode}`);
  } catch (error) {
    console.error('Error updating favorite:', error);
    throw error; 
  }
};

export const addFavoritoes = async (movieCode) => {
  try {
    console.log("Check Movie_code add:", movieCode);
    await api.post(`/favorites/add?movie_code=${movieCode}`);
  } catch (error) {
    console.error('Error added favorite:', error);
    throw error; 
  }
};

export default api;
