# Stabilizasyon ve Feature-First Mimari Geçişi Görevleri

## 1. Hazırlık ve Dosya Taşıma
- [ ] `UserEntity` ve `UserDao`'yu `:feature:auth` modülüne taşı
- [ ] `SearchHistoryEntity` ve `SearchHistoryDao`'yu `:feature:search` modülüne taşı
- [ ] Favori ve Liste ile ilgili tüm Entity ve DAO'ları `:feature:favorites` modülüne taşı
- [ ] `SessionManager`'ı `:core:data` içinde stabilize et

## 2. Veritabanı ve Bağımlılık Yapılandırması
- [ ] `MovieDatabase`'i `:app` modülüne taşı ve feature entity'lerini bağla
- [ ] Feature modüllerinin `build.gradle.kts` dosyalarına gerekli Room ve KSP bağımlılıklarını ekle
- [ ] `:app` modülünün tüm feature modüllerini gördüğünden emin ol

## 3. Dependency Injection (Hilt) Düzenlemesi
- [ ] Her feature için kendi içinde DAO ve API sağlayacak DI modülleri oluştur
- [ ] `:app` modülündeki `AppModule`'ü merkezi veritabanı sağlayacak şekilde güncelle

## 4. Temizlik ve Doğrulama
- [ ] `:core:data` ve `:core:domain` içindeki mükerrer/eski dosyaları sil
- [ ] `./gradlew clean` ve tam proje derlemesi yap
