# Fragman Oynatıcıyı Çalışır Hale Getirme Planı

Video oynatıcının siyah ekran vermesi ve "Invalid video id" hatası alması, WebView'ın yanlış yapılandırılmasından veya video yükleme sürecinin Compose yaşam döngüsüyle tam uyuşmamasından kaynaklanıyor olabilir. Bu sorunu çözmek için daha sağlam ve test edilmiş bir entegrasyon yöntemi uygulayacağız.

## Kullanıcı İncelemesi Gerekli

> [!IMPORTANT]
> Mevcut `android-youtube-player` kütüphanesini kullanmaya devam edeceğiz ancak kurulum yöntemini daha stabil olan `addYouTubePlayerListener` mekanizmasına döndüreceğiz. Ayrıca, video yükleme işlemini sadece bileşen gerçekten hazır olduğunda tetikleyeceğiz.

## Önerilen Değişiklikler

### Sunum Katmanı (Presentation Layer)

#### [MODIFY] [YouTubePlayer.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/components/YouTubePlayer.kt)
- **Lifecycle Yönetimi**: `LifecycleObserver` kullanımı `factory` içinde yapılandırılacak ve `onRelease` ile temizlenecek.
- **Initialization**: Kütüphanenin kendi otomatik başlatma mekanizması (`enableAutomaticInitialization = true`) kullanılacak.
- **Dinamik Güncelleme**: Eğer video ID'si değişirse (farklı filme geçilirse), oynatıcının yeni videoyu yüklemesi sağlanacak.

#### [MODIFY] [MovieDetailScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/MovieDetailScreen.kt)
- Fragman alanı yüksekliği videonun en-boy oranına (16:9) tam uyacak şekilde uyarlanacak.
- Video yüklenirken arka planda bir yükleme simgesi gösterilecek.
- Video alanının üzerindeki kontrollerin (Geri butonu, Favori butonu) videoya tıklamayı engellemediğinden emin olunacak.

### Veri Katmanı (Data Layer)

#### [MODIFY] [MoviesRepositoryImpl.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/data/repository/MoviesRepositoryImpl.kt)
- YouTube video kodlarının başında/sonunda olabilecek görünmez boşluklar (`trim()`) veri çekme aşamasında temizlenecek.

## Doğrulama Planı

### Manuel Doğrulama
- **The Godfather** veya **Spider-Man** gibi bilinen fragmanları olan filmlerde videonun açıldığı doğrulanacak.
- Videonun üzerine tıklandığında duraklatma/oynatma kontrollerinin çalıştığı kontrol edilecek.
- Cihaz yan yatırıldığında (landscape) video alanının genişlediği gözlemlenecek.
