import instance from './index';

/**
 * ログイン
 */
export function login(data: { id: string, password: string }) {
    return instance.post('/api/login', data);
}
