import type { RuleResult } from '@vuelidate/core'

export const collectErrors = (
  fieldName: string,
  isDirty: boolean,
  results: { rule: RuleResult; message: string }[],
): string[] => {
  if (!isDirty) {
    return []
  }

  return results
    .filter((res) => res.rule?.$invalid ?? false)
    .map((res) => res.message.replace('{attr}', fieldName))
}
