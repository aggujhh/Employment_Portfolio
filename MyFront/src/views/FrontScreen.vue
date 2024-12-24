<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="front_screen">
        <button @click="api_resetAllTables">reset</button>
        <main>
            <div class="header">
                <ul>
                    <li>
                        <div></div>空席：111
                    </li>
                    <li>
                        <div></div>客あり：111
                    </li>
                    <li>
                        <div></div>予約済：111
                    </li>
                    <li>
                        <div></div>利用不可：111
                    </li>
                    <li>
                        <div></div>仕上あり：111
                    </li>
                    <li>
                        <div></div>会計あり：111
                    </li>
                    <li>
                        <div></div>呼び込み：111
                    </li>
                </ul>
            </div>
            <div class="deskes_area">
                <div class="deskes deskes1">
                    <div>
                        <div class="desk" :style="tableStateArray['A1']">
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A1']">
                            <div class="center" :class="classNameArray['A1']">A1</div>
                            <div class="bar" v-if="progressBarArray['A1']?.isVisible">
                                <div>
                                    <div :style="{ width: progressBarArray['A1']?.progressBar + '%' }"></div>
                                </div>
                                <span>{{ progressBarArray['A1']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A1?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A1?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A2']">
                            <div class="center" :class="classNameArray['A2']">A2</div>
                            <div class="bar" v-if="progressBarArray['A2']?.isVisible">
                                <div>
                                    <div :style="{ width: progressBarArray['A2']?.progressBar + '%' }">
                                    </div>
                                </div>
                                <span>{{ progressBarArray['A2']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A2?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A2?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A3']">
                            <div class="center" :class="classNameArray['A3']">A3</div>
                            <div class="bar" v-if="progressBarArray['A3']?.isVisible">
                                <div>
                                    <div :style="{ width: progressBarArray['A3']?.progressBar + '%' }">
                                    </div>
                                </div>
                                <span>{{ progressBarArray['A3']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A3?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A3?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A4']">
                            <div class="center" :class="classNameArray['A4']">A4</div>
                            <div class="bar" v-if="progressBarArray['A4']?.isVisible">
                                <div>
                                    <div :style="{ width: progressBarArray['A4']?.progressBar + '%' }">
                                    </div>
                                </div>
                                <span>{{ progressBarArray['A4']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A4?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A4?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A5']">
                            <div class="center" :class="classNameArray['A5']">A5</div>
                            <div class="bar" v-if="progressBarArray['A5']?.isVisible">
                                <div>
                                    <div :style="{ width: progressBarArray['A5']?.progressBar + '%' }">
                                    </div>
                                </div>
                                <span>{{ progressBarArray['A5']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A5?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A5?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                </div>
                <div class="area">
                    <div class="deskes deskes3">
                        <div>
                            <div class="desk" :style="tableStateArray['C1']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C1?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C1?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C1']">C1</div>
                                <div class="bar" v-if="progressBarArray['C1']?.isVisible">
                                    <div>
                                        <div :style="{ width: progressBarArray['C1']?.progressBar + '%' }">
                                        </div>
                                    </div>
                                    <span>{{ progressBarArray['C1']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C2']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C2?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C2?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C2']">C2</div>
                                <div class="bar" v-if="progressBarArray['C2']?.isVisible">
                                    <div>
                                        <div :style="{ width: progressBarArray['C2']?.progressBar + '%' }">
                                        </div>
                                    </div>
                                    <span>{{ progressBarArray['C2']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C3']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C3?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C3?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C3']">C3</div>
                                <div class="bar" v-if="progressBarArray['C3']?.isVisible">
                                    <div>
                                        <div :style="{ width: progressBarArray['C3']?.progressBar + '%' }">
                                        </div>
                                    </div>
                                    <span>{{ progressBarArray['C3']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C4']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C4?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C4?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C4']">C4</div>
                                <div class="bar" v-if="progressBarArray['C4']?.isVisible">
                                    <div>
                                        <div :style="{ width: progressBarArray['C4']?.progressBar + '%' }">
                                        </div>
                                    </div>
                                    <span>{{ progressBarArray['C4']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="area">
                        <div class="deskes deskes5">
                            <div>
                                <div class="desk" :style="tableStateArray['E']">
                                    <div v-for="(seat, index) in Array.from({ length: seatStateArray?.E?.seatCount })"
                                        :key="index" class="seat" :style="index < seatStateArray?.E?.guestCount
                                            ? { background: '#FBB034' }
                                            : { background: '#505050' }" />
                                    <div class="center" :class="classNameArray['E']">E</div>
                                    <div class="bar" v-if="progressBarArray['E']?.isVisible">
                                        <div>
                                            <div :style="{ width: progressBarArray['E']?.progressBar + '%' }">
                                            </div>
                                        </div>
                                        <span>{{ progressBarArray['E']?.timeData }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="area">
                            <div class="deskes deskes4">
                                <div>
                                    <div class="desk" :style="tableStateArray['D1']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.D1?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.D1?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['D1']">D1</div>
                                        <div class="bar" v-if="progressBarArray['D1']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['D1']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['D1']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['D2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.D2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.D2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['D2']">D2</div>
                                        <div class="bar" v-if="progressBarArray['D2']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['D2']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['D2']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="deskes deskes2">
                                <div>
                                    <div class="desk" :style="tableStateArray['B1']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B1?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B1?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B1']">B1</div>
                                        <div class="bar" v-if="progressBarArray['B1']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['B1']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['B1']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B2']">B2</div>
                                        <div class="bar" v-if="progressBarArray['B2']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['B2']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['B2']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B3']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B3?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B3?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B3']">B3</div>
                                        <div class="bar" v-if="progressBarArray['B3']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['B3']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['B3']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B4']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B4?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B4?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B4']">B4</div>
                                        <div class="bar" v-if="progressBarArray['B4']?.isVisible">
                                            <div>
                                                <div :style="{ width: progressBarArray['B4']?.progressBar + '%' }">
                                                </div>
                                            </div>
                                            <span>{{ progressBarArray['B4']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <article>
            <div class="serving_info">
                <h3>料理の提供情報</h3>
                <ul>
                    <li completedorders>
                        <p>席番号</p>
                        <p>料理名</p>
                        <p>数量</p>
                        <p>確認</p>
                    </li>
                    <li v-for="(item, index) in completedorders" :key="index">
                        <p>{{ item.deskId }}</p>
                        <p>{{ item.dishName }}</p>
                        <p>{{ item.count }}</p>
                        <input type="checkbox">
                    </li>
                </ul>
            </div>
            <div class="business_info">
                <h3>営業情報</h3>
                <ul>
                    <li>
                        <p>今日の来店客数:</p>
                        <p>100人</p>
                    </li>
                    <li>
                        <p>今日の売上:</p>
                        <p>1000000円</p>
                    </li>
                    <li>
                        <p>今日の回転率:</p>
                        <p>213%</p>
                    </li>

                    <li>
                        <p>営業残り:</p>
                        <p>03:03</p>
                    </li>
                </ul>
            </div>
        </article>
    </section>
</template>

<script setup>
import { ref, onMounted, reactive, onBeforeUnmount, computed } from "vue"
import { fetchAllTables, resetAllTables } from "@/api/deskApi";

const tables = ref([]);
const api_fetchAllTables = async () => {
    try {
        const res = await fetchAllTables();
        tables.value = res.data.data; // APIからデータを取得
        console.log(tables.value);
        setTableState()
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

const api_resetAllTables = async () => {
    try {
        const res = await resetAllTables();
        console.log(res);
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            api_fetchAllTables();
        } else {
            alert(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("リセット失敗しました。もう一度お試しください。");
    }
}

// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    api_fetchAllTables();
    send_fetchAllCompletedOrders()
});

const getDeskStyle = (state) => {
    let background;
    let border;
    switch (state) {
        case '0':
            background = '#B7B7B7';
            break;
        case '1':
            background = '#5779FF';
            break;
        case '2':
            background = '#D62B81';
            break;
        case '3':
            background = '#fff';
            border = '1.5px dashed red'
            break;
    }
    return { background, border };
};
const seatStateArray = reactive({})
const tableStateArray = reactive({})
const classNameArray = reactive({})
const progressBarArray = reactive({})
const setTableState = () => {
    tables.value.forEach((item) => {
        tableStateArray[item.id] = getDeskStyle(item.deskState);
        classNameArray[item.id] = getOrderStateClassName(item.orderState)
        progressBarArray[item.id] = timeIntoProgressBar(item.orderTime)
        seatStateArray[item.id] = {
            seatCount: item.seatCount,
            guestCount: item.guestCount
        }
    });
}


/*************************************
* 時間を進捗バーに変換する
**************************************/
// 現在時刻を保存する ref
const now = ref(new Date());
// 現在時刻を1秒ごとに更新
setInterval(() => {
    now.value = new Date();
}, 1000);

// 進捗バーと時間データをリアルタイムで計算する
const timeIntoProgressBar = (time) => {
    return computed(() => {
        if (!time) {
            return { timeData: '00:00', progressBar: 0, isVisible: false };
        }
        const specifiedTime = new Date(time); // 注文時刻を Date オブジェクトに変換
        const timeDiff = now.value - specifiedTime; // 現在時刻との時間差を計算
        const totalSeconds = Math.floor(timeDiff / 1000); // 総秒数を計算
        const minutes = Math.floor((totalSeconds % 3600) / 60); // 分
        const seconds = totalSeconds % 60; // 秒
        const timeData = `${minutes}:${seconds.toString().padStart(2, '0')}`;

        // 進捗バーの計算（最大100）
        let progressBar = Math.floor(totalSeconds / 6);
        if (progressBar > 100) {
            progressBar = 100;
        }
        return { timeData, progressBar, isVisible: true };
    });
};




/*************************************
* オーナー状態により、classを変更
**************************************/
const getOrderStateClassName = (state) => {
    switch (state) {
        case '1':
            return 'state1';
        case '2':
            return 'state2';
        case '3':
            return 'state3';
    }
}


/*************************************
* 調理済オーダーをすべてフェッチ
**************************************/
import { fetchAllCompletedOrders } from "@/api/frontApi";
const completedorders = ref([])
const send_fetchAllCompletedOrders = async () => {
    try {
        const res = await fetchAllCompletedOrders();
        const code = res.data.code;
        if (code === 1) {
            completedorders.value = res.data.data
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("フェッチ失敗しました。もう一度お試しください。");
    }
}
/*************************************
* ユーザーが注文後の時、画面をリフレッシュ
* SSE（Server-Sent Events）を使用してリアルタイム更新を実現
**************************************/
import SseService from "@/utils/sseService";
/**
* SSE サービスのインスタンスを作成
* @param url サーバーの URL
* @param callback 新しいメッセージが届いた時の処理
*/
const sseService1 = new SseService("http://localhost:8080/api/kitchen/front", () => {
    // 注文リストをリフレッシュ
    api_fetchAllTables();
    send_fetchAllCompletedOrders()
});

const sseService2 = new SseService("http://localhost:8080/api/order/front", () => {
    // 注文リストをリフレッシュ
    api_fetchAllTables();
});

// SSE 接続を開始
sseService1.connect();
sseService2.connect();
//コンポーネントが破棄される前に呼び出されるフック
onBeforeUnmount(() => {
    // SSE 接続を閉じる
    sseService1.close();
    sseService2.close();
});
</script>

<style lang="less" scoped>
@import '@/assets/css/front_screen.less';
</style>
