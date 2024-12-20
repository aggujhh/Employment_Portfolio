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
                        <div></div>注文あり：111
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
                            <div class="center">A1</div>
                            <div class="bar">
                                <div></div>
                                <span>00:00</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A1?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A1?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A2']">
                            <div class="center">A2</div>
                            <div class="bar">
                                <div></div>
                                <span>00:00</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A2?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A2?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A3']">
                            <div class="center">A3</div>
                            <div class="bar">
                                <div></div>
                                <span>00:00</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A3?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A3?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A4']">
                            <div class="center">A4</div>
                            <div class="bar">
                                <div></div>
                                <span>00:00</span>
                            </div>
                            <div v-for="(seat, index) in Array.from({ length: seatStateArray?.A4?.seatCount })"
                                :key="index" class="seat" :style="index < seatStateArray?.A4?.guestCount
                                    ? { background: '#FBB034' }
                                    : { background: '#505050' }" />
                        </div>
                    </div>
                    <div>
                        <div class="desk" :style="tableStateArray['A5']">
                            <div class="center">A5</div>
                            <div class="bar">
                                <div></div>
                                <span>00:00</span>
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
                                <div class="center">C1</div>
                                <div class="bar">
                                    <div></div>
                                    <span>00:00</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C2']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C2?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C2?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center">C2</div>
                                <div class="bar">
                                    <div></div>
                                    <span>00:00</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C3']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C3?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C3?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center">C3</div>
                                <div class="bar">
                                    <div></div>
                                    <span>00:00</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="desk" :style="tableStateArray['C4']">
                                <div v-for="(seat, index) in Array.from({ length: seatStateArray?.C4?.seatCount })"
                                    :key="index" class="seat" :style="index < seatStateArray?.C4?.guestCount
                                        ? { background: '#FBB034' }
                                        : { background: '#505050' }" />
                                <div class="center">C4</div>
                                <div class="bar">
                                    <div></div>
                                    <span>00:00</span>
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
                                    <div class="center">E</div>
                                    <div class="bar">
                                        <div></div>
                                        <span>00:00</span>
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
                                        <div class="center">D1</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['D2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.D2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.D2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center">D2</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
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
                                        <div class="center">B1</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B2']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B2?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B2?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center">B2</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B3']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B3?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B3?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center">B3</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="desk" :style="tableStateArray['B4']">
                                        <div v-for="(seat, index) in Array.from({ length: seatStateArray?.B4?.seatCount })"
                                            :key="index" class="seat" :style="index < seatStateArray?.B4?.guestCount
                                                ? { background: '#FBB034' }
                                                : { background: '#505050' }" />
                                        <div class="center">B4</div>
                                        <div class="bar">
                                            <div></div>
                                            <span>00:00</span>
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
                    <li>
                        <p>席番号</p>
                        <p>料理名</p>
                        <p>数量</p>
                        <p>確認</p>
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
                        <input type="checkbox">
                    </li>
                    <li>
                        <p>C4</p>
                        <p>前菜の盛り合わせ</p>
                        <p>4</p>
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
import { ref, onMounted, reactive } from "vue"
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
            console.log(res.data.msg);
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
const setTableState = () => {
    tables.value.forEach((item) => {
        tableStateArray[item.id] = getDeskStyle(item.deskState);
        seatStateArray[item.id] = {
            seatCount: item.seatCount,
            guestCount: item.guestCount
        }
    });
    console.log(seatStateArray);
}
</script>

<style lang="less" scoped>
@import '@/assets/css/front_screen.less';
</style>
