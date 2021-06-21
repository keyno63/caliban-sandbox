# caliban-sandbox

[caliban](https://github.com/ghostdogpr/caliban) の動作確認用につくったレポジトリーです。　　

# 概要
## ライブラリについて
caliban は優れた Scala 用 GraphQL ライブラリです。  
どう優れているかというと以下の点があります。  
- ボイラープレートをなるべく排除した実装を可能にしている
- 純粋関数型ライブラリを目指した設計をしています
- Scala の型で GraphQL の設計ができます
- ZIO を使ったライブラリなので、ZIO の実装方法について大変参考になります
- client と server 機能の両方があります

## 目標
その caliban を使って以下のことを成し遂げたい  
- 純粋に caliban の実装の確認
- 改造した部分の動作確認
- 各種デプロイ方法の確認（local 動作、PaaSデプロイ、CIビルド）

