# KSP Hatalarını Giderme ve Mimari Stabilizasyon Planı

KSP (Kotlin Symbol Processing) hataları genellikle dairesel bağımlılıklar (circular dependencies), mükerrer sınıf tanımları veya Room/Hilt konfigürasyon hatalarından kaynaklanır. Mevcut durumda, dosyaların modüller arasında taşınması sırasında oluşan karmaşa bu hatalara yol açıyor.

## Kullanıcı İncelemesi Gerekenler

> [!IMPORTANT]
> Projeyi "Tam Özellik Odaklı" (Pure Feature-First) yapıya taşırken karşılaşılan en büyük engel, Room veritabanının tüm tabloları tek bir `@Database` sınıfında toplama zorunluluğudur. Bu durum dairesel bağımlılıklara (`core:data` -> `feature:auth` -> `core:data`) neden olmaktadır.

> [!TIP]
> Bu sorunu çözmek için **Database tanımını `:app` modülüne taşıyacağız.** Böylece feature modülleri veritabanına bağımlı olmayacak, sadece kendi DAO'larını Hilt üzerinden enjekte edecekler.

## Önerilen Değişiklikler

### 1. Bağımlılık Döngülerini Kırmak
- Feature modüllerinin (auth, movies, search, favorites) `:core:data` modülüne olan bağımlılıkları kaldırılacak.
- `:core:data` modülü, sadece ortak kullanılan verileri (örn: `SessionManager`) barındıracak veya tamamen boşaltılacak.

### 2. Room Yapılandırmasını Düzenlemek
- **Entity ve DAO'lar:** Her biri ait olduğu feature modülüne taşınacak.
    - `UserEntity`/`UserDao` -> `:feature:auth`
    - `SearchHistoryEntity`/`SearchHistoryDao` -> `:feature:search`
    - `FavoriteMovieEntity`/`UserListEntity`/`UserListDao`/`FavoriteMovieDao` -> `:feature:favorites`
- **MovieDatabase:** `:app` modülüne taşınacak. Bu modül tüm feature modüllerini gördüğü için dairesel bağımlılık oluşmayacak.

### 3. Hilt ve DI Temizliği
- Her feature modülü kendi API ve Repository implementasyonlarını kendi içindeki bir Hilt modülü ile sağlayacak.
- Merkezi `AppModule` (`:app` içinde), Room veritabanını ve DAO'ları sağlayacak.

## Doğrulama Planı

### Otomatik Testler
- `./gradlew clean` komutu ile tüm önbellekler temizlenecek.
- `assembleDebug` ile projenin tamamının hatasız derlendiği doğrulanacak.

### Manuel Doğrulama
- "Movies içi boş" sorununun giderildiği (modellerin ve repository mantığının modül içine taşındığı) kontrol edilecek.
- KSP hatalarının tamamen kaybolduğu teyit edilecek.
