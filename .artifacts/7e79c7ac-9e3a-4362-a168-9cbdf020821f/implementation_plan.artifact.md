# Popüler Filmlerin İlk 3'ünü Bannerda Gösterme Planı

`MovieViewModel` içindeki `fetchBannerMovies` fonksiyonu şu an `repository.getPopularMovies()` çağrısı yapıyor. Ancak bu fonksiyon `Flow<PagingData<MovieDto>>` döndürdüğü için `RestResult` ile karşılaştırılamıyor ve derleme hatasına yol açıyor. Ayrıca kullanıcı, banner alanında "Popüler" filmlerin ilk 3'ünü görmek istiyor.

## Değişiklik Özeti

1.  **Repository Güncellemesi**: `MovieRepositoryImpl` içindeki `getBannerMovies` fonksiyonu şu an "Top Rated" filmleri getiriyor. Bunu "Popular" filmleri getirecek şekilde güncelleyeceğiz.
2.  **ViewModel Güncellemesi**: `MovieViewModel` içindeki hatalı çağrıyı `repository.getBannerMovies()` ile düzelteceğiz.

## Önerilen Değişiklikler

### [core:data]

#### [MODIFY] [MovieRepositoryImpl.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/data/src/main/java/com/example/movieapp/data/repository/MovieRepositoryImpl.kt)
- `getBannerMovies` fonksiyonu içinde `apiService.getTopRatedMovies(page = 1)` yerine `apiService.getPopularMovies(page = 1)` kullanılacak.

### [feature:movies]

#### [MODIFY] [MovieViewModel.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/MovieViewModel.kt)
- `fetchBannerMovies` fonksiyonu içindeki `repository.getPopularMovies()` çağrısı `repository.getBannerMovies()` olarak değiştirilecek.

## Doğrulama Planı

### Manuel Doğrulama
- `MovieViewModel` dosyasının derleme hatası vermediği kontrol edilecek.
- Banner alanında popüler filmlerin ilk 3 tanesinin göründüğü (mantıksal olarak kod üzerinden) teyit edilecek.
