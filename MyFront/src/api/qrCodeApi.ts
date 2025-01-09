import instance from './index';

/**
 * QRコードをすべてフェッチ
 */
export function fetchAllQrCode() {
    return instance.get('/api/qrCode');
}

