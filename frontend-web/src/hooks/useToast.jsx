import { useCallback, useState } from 'react';

export function useToast() {
  const [toast, setToast] = useState(null);

  const showToast = useCallback((message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  }, []);

  const Toast = () =>
    toast ? (
      <div className={`toast toast-${toast.type}`}>{toast.message}</div>
    ) : null;

  return { showToast, Toast };
}
