export type SnackbarMessage = {
  text: string
  type: 'error' | 'warning' | 'success' | 'info'
}

class AsyncExecutor<T> {
  constructor(
    private action: () => Promise<T | null>,
    private success: string,
    private error: string | null,
  ) {}

  async execute(): Promise<{ message: SnackbarMessage; result: T | null }> {
    try {
      const result = await this.action()
      return { message: { text: this.success, type: 'success' }, result }
    } catch (err: Error | unknown) {
      console.error(err)
      return {
        message: {
          text: this.error ?? (err instanceof Error ? err.message : 'Error'),
          type: 'error',
        },
        result: null,
      }
    }
  }
}

export class AsyncExecutorBuilder<T> {
  private fn: () => Promise<T | null> = () => Promise.resolve(null)
  private successMessage: string = 'The action has been executed successfully'
  private errorMessage: string | null = null

  static asyncExecutorBuilder<T>(): AsyncExecutorBuilder<T> {
    return new AsyncExecutorBuilder<T>()
  }

  action(fn: () => Promise<T | null>): AsyncExecutorBuilder<T> {
    this.fn = fn
    return this
  }

  success(message: string): AsyncExecutorBuilder<T> {
    this.successMessage = message
    return this
  }

  error(message: string): AsyncExecutorBuilder<T> {
    this.errorMessage = message
    return this
  }

  build(): AsyncExecutor<T> {
    return new AsyncExecutor<T>(this.fn, this.successMessage, this.errorMessage)
  }
}
