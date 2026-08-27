# Fragman Hata Görünümü ve Yükleme İyileştirmesi

Film detay ekranında fragmanların yüklenememe sorunu giderildi ve hata anında afişe dönmek yerine siyah hata ekranının kalması sağlandı.

## Yapılan Değişiklikler

### [YouTubePlayer.kt]
- **Kütüphane Geçişi**: Manuel WebView yöntemi bırakılıp `android-youtube-player` kütüphanesine geçildi. Bu sayede fragmanlar daha kararlı yükleniyor ve "UNKNOWN" hataları azaldı.
- **Yaşam Döngüsü**: Oynatıcı Android Lifecycle ile tam uyumlu çalışıyor.

### [MovieDetailScreen.kt]
- **Hata Ekranı Restorasyonu**: Fragman yüklenirken hata oluşursa (`isTrailerError`), uygulama artık film afişine (poster) geri dönmüyor. Bunun yerine siyah zemin üzerinde uyarı ikonu ve "Fragman yüklenemedi" mesajı gösteriliyor.

## Görsel Mantık Özeti
1. Film için fragman anahtarı varsa:
   - Başarılıysa -> Fragmanı oynat.
   - Hata oluşursa -> **Siyah Hata Ekranını göster (Afiş gelmez).**
2. Film için hiç fragman anahtarı yoksa:
   - Standart davranış -> Film afişini (poster) göster.
