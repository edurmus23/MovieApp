# Fragman Yükleme Sorununun Giderilmesi

Fragmanların "Trailer could not be loaded" hatası vermesinin sebebi, WebView üzerinden manuel olarak yönetilen YouTube IFrame API'nın bazı cihazlarda/videolarda kısıtlamalara takılması veya yaşam döngüsüyle uyumsuz çalışmasıdır. Bu sorunu çözmek için projede halihazırda bağımlılık olarak ekli olan `android-youtube-player` kütüphanesini kullanacağız.

## Kullanıcı İncelemesi Gerekli

> [!IMPORTANT]
> Bu değişiklik, manuel WebView implementasyonunu kaldırıp yerine daha kararlı ve Android yaşam döngüsüyle uyumlu çalışan resmi olmayan ama standart kabul edilen `com.pierfrancescosoffritti.androidyoutubeplayer` kütüphanesini getirecektir.

## Önerilen Değişiklikler

### [Movies Feature]

`YouTubePlayer.kt` dosyasını kütüphaneyi kullanacak şekilde güncelleyeceğiz.

#### [MODIFY] [YouTubePlayer.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/components/YouTubePlayer.kt)
- `WebView` yerine `YouTubePlayerView` kullanılacak.
- `AbstractYouTubePlayerListener` ile video yükleme ve hata yönetimi yapılacak.
- `LifecycleOwner` üzerinden oynatıcının yaşam döngüsü (resume/pause/stop) otomatik yönetilecek.
- Hata durumunda `onError` callback'i tetiklenmeye devam edecek.

## Doğrulama Planı

### Manuel Doğrulama
1. Fragmanlı bir film açın.
2. Fragmanın otomatik olarak başladığını ve oynatıldığını doğrulayın.
3. İnterneti kapatıp hata mesajının düzgün göründüğünü kontrol edin.
