// このファイルを設定することで、エラーを解決できます: モジュール「@/views/Login.vue」またはその対応する型宣言が見つかりません。ts(2307)
// このコードは TypeScript に対し、.vue で終わるすべてのファイルが Vue コンポーネントであることを伝えます。
// これにより、import 文を使用してモジュールをインポートできるようになり、通常モジュールを認識できない問題を解決します。

declare module "*.vue" {
  import { Component } from "vue";
  const component: Component;
  export default component;
}
