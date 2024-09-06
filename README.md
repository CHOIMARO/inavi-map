# inavi-map
아이나비 맵 SDK와 API를 사용하여 구성한 앱

## 모듈 구성
###App Layer (UI)
  Composable UI
  ViewModels
  UI 관련 모델

###Domain
  Use Cases
  Domain Models
  Repository Interfaces

##Data
  Repository Implementations
  Data Sources (Remote, Local)
  Data Models
  Service
##Inavi-map-compose
  Converted INAVI MAP SDK To Compose -> 아이나비 SDK는 xml에 최적화 되어 있어 Compose에서 사용하기 쉽도록 바꾸었음.

##Inavi-map-location
  location -> 위치 추적 관련하여 google API를 사용하기 위해 따로 빼서 관리
