<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="front_screen">
        <button @click="openSettingDialog">
            <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24">
                <g fill="none" stroke="#616161" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5">
                    <path class="path1" d="M12.132 15.404a3.364 3.364 0 1 0 0-6.728a3.364 3.364 0 0 0 0 6.728" />
                    <path
                        d="M20.983 15.094a9.4 9.4 0 0 1-1.802 3.1l-2.124-.482a7.25 7.25 0 0 1-2.801 1.56l-.574 2.079a9.5 9.5 0 0 1-1.63.149a9 9 0 0 1-2.032-.23l-.609-2.146a7.5 7.5 0 0 1-2.457-1.493l-2.1.54a9.4 9.4 0 0 1-1.837-3.33l1.55-1.722a7.2 7.2 0 0 1 .069-2.652L3.107 8.872a9.4 9.4 0 0 1 2.067-3.353l2.17.54A7.7 7.7 0 0 1 9.319 4.91l.574-2.124a9 9 0 0 1 2.17-.287c.585 0 1.17.054 1.745.16l.551 2.113c.83.269 1.608.68 2.296 1.217l2.182-.563a9.4 9.4 0 0 1 2.043 3.1l-1.48 1.607a7.4 7.4 0 0 1 .068 3.364z" />
                </g>
            </svg>
        </button>
        <main>
            <div class="header">
                <ul>
                    <li>
                        <div></div>空席：{{ stateCounts.deskState0 }}
                    </li>
                    <li>
                        <div></div>客あり：{{ stateCounts.deskState1 }}
                    </li>
                    <li>
                        <div></div>予約済：{{ stateCounts.deskState2 }}
                    </li>
                    <li>
                        <div></div>利用不可：{{ stateCounts.deskState3 }}
                    </li>
                    <li>
                        <div></div>仕上あり：{{ stateCounts.orderState1 }}
                    </li>
                    <li>
                        <div></div>会計あり：{{ stateCounts.orderState2 }}
                    </li>
                    <li>
                        <div></div>呼び出し：{{ stateCounts.orderState3 }}
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
                            <div class="center" :class="classNameArray['A1']" @click="openDialog('A1')">
                                A1</div>
                            <div class="bar" v-if="progressBarArray['A1']?.isVisible">
                                <div>
                                    <div :style="progressBarArray['A1']?.progressBarStyle" />
                                </div>
                                <span :style="{ color: progressBarArray['A1']?.timeColor }">{{
                                    progressBarArray['A1']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A1?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A1?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A2']">
                            <div class="center" :class="classNameArray['A2']" @click="openDialog('A2')">A2</div>
                            <div class="bar" v-if="progressBarArray['A2']?.isVisible">
                                <div>
                                    <div :style="progressBarArray['A2']?.progressBarStyle" />
                                </div>
                                <span :style="{ color: progressBarArray['A2']?.timeColor }">{{
                                    progressBarArray['A2']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A2?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A2?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A3']">
                            <div class="center" :class="classNameArray['A3']" @click="openDialog('A3')">A3</div>
                            <div class="bar" v-if="progressBarArray['A3']?.isVisible">
                                <div>
                                    <div :style="progressBarArray['A3']?.progressBarStyle" />
                                </div>
                                <span :style="{ color: progressBarArray['A3']?.timeColor }">{{
                                    progressBarArray['A3']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A3?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A3?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A4']">
                            <div class="center" :class="classNameArray['A4']" @click="openDialog('A4')">A4</div>
                            <div class="bar" v-if="progressBarArray['A4']?.isVisible">
                                <div>
                                    <div :style="progressBarArray['A4']?.progressBarStyle" />
                                </div>
                                <span :style="{ color: progressBarArray['A4']?.timeColor }">{{
                                    progressBarArray['A4']?.timeData }}</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A4?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A4?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A5']">
                            <div class="center" :class="classNameArray['A5']" @click="openDialog('A5')">A5</div>
                            <div class="bar" v-if="progressBarArray['A5']?.isVisible">
                                <div>
                                    <div :style="progressBarArray['A5']?.progressBarStyle" />
                                </div>
                                <span :style="{ color: progressBarArray['A5']?.timeColor }">{{
                                    progressBarArray['A5']?.timeData }}</span>
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
                                <div class="center" :class="classNameArray['C1']" @click="openDialog('C1')">C1</div>
                                <div class="bar" v-if="progressBarArray['C1']?.isVisible">
                                    <div>
                                        <div :style="progressBarArray['C1']?.progressBarStyle" />
                                    </div>
                                    <span :style="{ color: progressBarArray['C1']?.timeColor }">{{
                                        progressBarArray['C1']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C2']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C2?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C2?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C2']" @click="openDialog('C2')">C2</div>
                                <div class="bar" v-if="progressBarArray['C2']?.isVisible">
                                    <div>
                                        <div :style="progressBarArray['C2']?.progressBarStyle" />
                                    </div>
                                    <span :style="{ color: progressBarArray['C2']?.timeColor }">{{
                                        progressBarArray['C2']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C3']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C3?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C3?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C3']" @click="openDialog('C3')">C3</div>
                                <div class="bar" v-if="progressBarArray['C3']?.isVisible">
                                    <div>
                                        <div :style="progressBarArray['C3']?.progressBarStyle" />
                                    </div>
                                    <span :style="{ color: progressBarArray['C3']?.timeColor }">{{
                                        progressBarArray['C3']?.timeData }}</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C4']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C4?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C4?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center" :class="classNameArray['C4']" @click="openDialog('C4')">C4</div>
                                <div class="bar" v-if="progressBarArray['C4']?.isVisible">
                                    <div>
                                        <div :style="progressBarArray['C4']?.progressBarStyle" />
                                    </div>
                                    <span :style="{ color: progressBarArray['C4']?.timeColor }">{{
                                        progressBarArray['C4']?.timeData }}</span>
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
                                    <div class="center" :class="classNameArray['E']" @click="openDialog('E')">E</div>
                                    <div class="bar" v-if="progressBarArray['E']?.isVisible">
                                        <div>
                                            <div :style="progressBarArray['E']?.progressBarStyle" />
                                        </div>
                                        <span :style="{ color: progressBarArray['E']?.timeColor }">{{
                                            progressBarArray['E']?.timeData }}</span>
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
                                        <div class="center" :class="classNameArray['D1']" @click="openDialog('D1')">D1
                                        </div>
                                        <div class="bar" v-if="progressBarArray['D1']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['D1']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['D1']?.timeColor }">{{
                                                progressBarArray['D1']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['D2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.D2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.D2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['D2']" @click="openDialog('D2')">D2
                                        </div>
                                        <div class="bar" v-if="progressBarArray['D2']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['D2']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['D2']?.timeColor }">{{
                                                progressBarArray['D2']?.timeData }}</span>
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
                                        <div class="center" :class="classNameArray['B1']" @click="openDialog('B1')">B1
                                        </div>
                                        <div class="bar" v-if="progressBarArray['B1']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['B1']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['B1']?.timeColor }">{{
                                                progressBarArray['B1']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B2']" @click="openDialog('B2')">B2
                                        </div>
                                        <div class="bar" v-if="progressBarArray['B2']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['B2']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['B2']?.timeColor }">{{
                                                progressBarArray['B2']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B3']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B3?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B3?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B3']" @click="openDialog('B3')">B3
                                        </div>
                                        <div class="bar" v-if="progressBarArray['B3']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['B3']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['B3']?.timeColor }">{{
                                                progressBarArray['B3']?.timeData }}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B4']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B4?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B4?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center" :class="classNameArray['B4']" @click="openDialog('B4')">B4
                                        </div>
                                        <div class="bar" v-if="progressBarArray['B4']?.isVisible">
                                            <div>
                                                <div :style="progressBarArray['B4']?.progressBarStyle" />
                                            </div>
                                            <span :style="{ color: progressBarArray['B4']?.timeColor }">{{
                                                progressBarArray['B4']?.timeData }}</span>
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
                        <input type="checkbox" :checked="item.checked"
                            @change="send_changeOrderDishState(index, item.deskId, item.orderId, item.dishId)">
                    </li>
                </ul>
            </div>
            <div class="business_info">
                <h3>営業情報</h3>
                <ul>
                    <li>
                        <p>今日の来店客数:</p>
                        <p>{{ operations.customerCount }}人</p>
                    </li>
                    <li>
                        <p>今日の売上:</p>
                        <p>{{ operations.sales }}円</p>
                    </li>
                    <li>
                        <p>今日の回転率:</p>
                        <p>{{ operations.turnoverRate }}%</p>
                    </li>

                    <li>
                        <p>営業残り:</p>
                        <p>{{ operations.closingTime }}</p>
                    </li>
                </ul>
            </div>
        </article>
    </section>
    <AccountingConfirmationDialog :isVisible="isVisible.accountingConfirmation" :deskId="deskId" @close="closeModal" />
    <CallDialog :isVisible="isVisible.call" :deskId="deskId" @close="closeModal" />
    <FrontSettingDialog :isVisible="isVisible.setting" @close="closeModal" />
</template>

<script setup>
import { ref, onMounted, reactive, onBeforeUnmount, computed } from "vue"
import { fetchAllTables} from "@/api/deskApi";


const tables = ref([]);


const api_fetchAllTables = async () => {
    try {
        const res = await fetchAllTables();
        tables.value = res.data.data; // APIからデータを取得
        setTableState()
        setHeader()
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

const stateCounts = reactive({
    deskState0: 0,
    deskState1: 0,
    deskState2: 0,
    deskState4: 0,
    orderState1: 0,
    orderState2: 0,
    orderState3: 0
})
const setHeader = () => {
    stateCounts.deskState0 = 0
    stateCounts.deskState1 = 0
    stateCounts.deskState2 = 0
    stateCounts.deskState3 = 0
    stateCounts.orderState1 = 0
    stateCounts.orderState2 = 0
    stateCounts.orderState3 = 0
    tables.value.forEach(item => {
        switch (item.deskState) {
            case '0':
                stateCounts.deskState0++;
                break;
            case '1':
                stateCounts.deskState1++;
                break;
            case '2':
                stateCounts.deskState2++;
                break;
            case '3':
                stateCounts.deskState3++;
                break;
        }
        switch (item.orderState) {
            case '1':
                stateCounts.orderState1++;
                break;
            case '2':
                stateCounts.orderState2++;
                break;
            case '3':
                stateCounts.orderState3++;
                break;
        }
    })
}



// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    api_fetchAllTables();
    send_fetchAllCompletedOrders()
    send_fetchOperations()
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
const orderStateArray = reactive({})


const setTableState = () => {
    tables.value.forEach((item) => {
        tableStateArray[item.id] = getDeskStyle(item.deskState);
        classNameArray[item.id] = getOrderStateClassName(item.orderState)
        progressBarArray[item.id] = timeIntoProgressBar(item.orderTime)
        seatStateArray[item.id] = {
            seatCount: item.seatCount,
            guestCount: item.guestCount
        }
        orderStateArray[item.id] = item.orderState
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
            return { isVisible: false };
        }
        const specifiedTime = new Date(time); // 注文時刻を Date オブジェクトに変換
        const timeDiff = now.value - specifiedTime; // 現在時刻との時間差を計算
        const totalSeconds = Math.floor(timeDiff / 1000); // 総秒数を計算
        const minutes = Math.floor(totalSeconds / 60); // 分
        const seconds = totalSeconds % 60; // 秒
        const timeData = `${minutes}:${seconds.toString().padStart(2, '0')}`;

        let progressBar = Math.floor(totalSeconds / 6);
        let timeColor = '#333'
        let progressBarStyle = {
            width: progressBar + '%',
            backgroundColor: '#fff'
        }
        // 進捗バーの計算（最大100）
        if (progressBar > 100) {
            timeColor = 'red'
            progressBarStyle = {
                width: '100%',
                backgroundColor: '#FF8080',
                border: '0.5px solid red'
            }
        }
        return { timeData, timeColor, progressBarStyle, isVisible: true };
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
            completedorders.value = res.data.data.map(item => ({
                ...item,
                checked: false, // 每次重新加载时重置 checked 状态
            }));
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

/*************************************
* 料理状態を提供済みに変更
**************************************/
import { changeOrderDishState } from "@/api/frontApi";
const send_changeOrderDishState = async (index, deskId, orderId, dishId) => {
    try {
        await new Promise((resolve) => setTimeout(resolve, 300));
        const res = await changeOrderDishState({ index, deskId, orderId, dishId });
        const code = res.data.code;
        if (code === 1) {
            completedorders.value[index].checked = !completedorders.value[index].checked;
            send_fetchAllCompletedOrders()
            api_fetchAllTables();
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
* 会計確認と呼び出し確認
**************************************/
import AccountingConfirmationDialog from "@/components/AccountingConfirmationDialog.vue";
import CallDialog from "@/components/CallDialog.vue";
import FrontSettingDialog from "@/components/FrontSettingDialog.vue";

const isVisible = reactive({
    accountingConfirmation: false,
    call: false,
    setting: false
})
// モーダルを閉じる関数
const closeModal = () => {
    isVisible.accountingConfirmation = false;
    isVisible.call = false;
    isVisible.setting = false;
    api_fetchAllTables();
    send_fetchOperations();
};

const deskId = ref()

const openDialog = (id) => {
    console.log("orderStateArray[id]", orderStateArray[id]);
    if (orderStateArray[id] === '2') {
        openAccountingConfirmationDialog(id);
    } else if (orderStateArray[id] === '3') {
        openCallDialog(id)
    }
}

const openSettingDialog = () => {
    isVisible.setting = true;
}

const openAccountingConfirmationDialog = (id) => {
    deskId.value = id
    isVisible.accountingConfirmation = true;
}

const openCallDialog = (id) => {
    deskId.value = id
    isVisible.call = true;
}


/*************************************
* 営業情報所得する
**************************************/
import { fetchOperations } from "@/api/frontApi";
const operations = reactive({
    customerCount: "",
    sales: "",
    turnoverRate: "",
    closingTime: ""
});
const send_fetchOperations = async () => {
    try {
        const res = await fetchOperations();
        const code = res.data.code;
        if (code === 1) {
            Object.assign(operations, res.data.data);
            setClosingTime()
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("営業情報フェッチ失敗しました。もう一度お試しください。");
    }
}

/*************************************
* ラストタイムアウトを設定する
**************************************/
const setClosingTime = () => {
    const updateClosingTime = () => {
        const now = new Date();
        const currentDate = now.toISOString().split("T")[0];

        // 获取午餐和晚餐的开始和结束时间
        const lunchStartTime = new Date(`${currentDate}T${operations.lunchTimeStart}`);
        const lunchEndTime = new Date(`${currentDate}T${operations.lunchTimeEnd}`);
        const dinnerStartTime = new Date(`${currentDate}T${operations.dinnerTimeStart}`);
        const dinnerEndTime = new Date(`${currentDate}T${operations.dinnerTimeEnd}`);

        // 当前时间是否在午餐或晚餐时间段内
        if (now >= lunchStartTime && now <= lunchEndTime) {
            const lunchRemaining = lunchEndTime - now;
            const hours = Math.floor(lunchRemaining / (1000 * 60 * 60));
            const minutes = Math.floor((lunchRemaining % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((lunchRemaining % (1000 * 60)) / 1000);
            operations.closingTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
            return;
        }

        if (now >= dinnerStartTime && now <= dinnerEndTime) {
            const dinnerRemaining = dinnerEndTime - now;
            const hours = Math.floor(dinnerRemaining / (1000 * 60 * 60));
            const minutes = Math.floor((dinnerRemaining % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((dinnerRemaining % (1000 * 60)) / 1000);
            operations.closingTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
            return;
        }

        // 不在午餐或晚餐时间段内
        operations.closingTime = "営業時間外";
    };

    updateClosingTime();
    // 然后启动定时器
    const interval = setInterval(updateClosingTime, 1000);
}



</script>

<style lang="less" scoped>
@import '@/assets/css/front_screen.less';
</style>
