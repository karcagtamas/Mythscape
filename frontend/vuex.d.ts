import { Store } from 'vuex'

declare module 'vue' {
  interface State {
    counter: number
  }

  interface ComponentCustomProperties {
    $store: Store<State>
  }
}
