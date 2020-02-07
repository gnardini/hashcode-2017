#pragma GCC optimize("Ofast")
//#pragma comment(linker, "/STACK: 20000000")
#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
#include <bits/stdc++.h>
using namespace std;
#define ll long long int
#define ld long double
#define vec vector
#define all(x) x.begin(), x.end()
#define dbg(x) cerr<<#x<<" = "<<x<<endl
#define IOS ios::sync_with_stdio(false);cin.tie(0);cout.tie(0);

struct warehouse{
    int x,y;
    map<int,int> item_times;
};

struct orden{
    map<int,int> item_times;
    bool operator<(const orden &other)const{return item_times.size()<other.item_times.size();}
    bool operator==(const orden &other)const{return item_times.size()==other.item_times.size();}
};

struct dron{
    int x,y;
    int t;
    bool operator<(const dron &other)const{return t<other.t;}
    bool operator==(const dron &other)const{return t==other.t;}
};

ld dist(ll x1, ll y1, ll x2, ll y2){
    return sqrt( (x1-y1)*(x1-y1) + (x2-y2)*(x2-y2) );
}

int main(){
    int n,m; cin>>n>>m;
    int D; cin>>D;
    int T; cin>>T;
    int D_size; cin>>D_size;
    int types; cin>>types;
    ll score = 0;
    vec<int> types_costs(types);
    for(int i=0;i<types;i++) cin>>types_costs[i];
    int W; cin>>W;

    vec<warehouse> warehouses(W); //elimina los vacios despues
    for(int i=0;i<W;i++){
        int x,y; cin>>x>>y;
        map<int,int> items;
        for(int i=0;i<types;i++){int k;cin>>k;items[k]++;}
        warehouses[i] = {x,y,items};
    }

    int Q; cin>>Q;
    multiset<orden> ordenes;
    for(int i=0;i<Q;i++){
        orden q;
        int x,y;cin>>x>>y;
        int j;cin>>j;
        map<int,int> items;
        for(int k=0;k<j;k++){int kk;cin>>kk;items[kk]++;}
    }

    multiset<dron> drones;
    for(int i=0;i<D;i++) drones.insert({warehouses[0].x,warehouses[0].y,0});

    while(ordenes.size()){
        orden orden_actual = *ordenes.begin();
        for(auto &it: drones){
            //me fijo en algunos warehouses
            int best_warehouse = 0;
            int best_warehouse_score = -1;
            for(int i=0;i*i<warehouses.size();i++){ // O(sqrt(n))
                int try_warehouse = rand()%warehouses.size();
                ld try_warehouse_score = 0;
                int peso = 0;
                for(auto p : orden_actual.item_times){
                    int agrego = min( p.second, warehouses[ try_warehouse ].item_times[ p.first ] );
                    if( peso + agrego * types_costs[ p.first ] > D_size ){ //contemplo si me pase del tam del dron
                        while(agrego--){
                            if ( peso + agrego * types_costs[ p.first ] <= D_size ){
                                peso +=  agrego * types_costs[ p.first ];
                                break;
                            }
                        }
                    }else
                        peso += agrego * types_costs[ p.first ];
                    try_warehouse_score += peso;
                }
                try_warehouse_score /= dist(it.x,it.y,warehouses[try_warehouse].x,warehouses[try_warehouse].y);

                if( try_warehouse_score > best_warehouse_score ){
                    best_warehouse_score = try_warehouse_score;
                    best_warehouse = try_warehouse;
                }
            }
            map<int,int> agrego;
            for(auto p: orden_actual.item_times){ // actualizo warehouse
                agrego[ p.first ] = min( p.second, warehouses[ best_warehouse ].item_times[ p.first ] );
                warehouses[ best_warehouse ].item_times[ p.first ] -= agrego[ p.first ];
            }
            //actualizo orden, si quedo vacia actualizo score
            for(auto p: agrego) orden_actual.item_times[ p.first ] -= p.second;
            bool vacia = true;
            for(auto p: orden_actual.item_times) if(p.second!=0){vacia=false;break;}
            //actualizo dron
            dron itt = it;
            itt.t += dist(it.x, it.y, warehouses[best_warehouse].x, warehouses[best_warehouse].y);
            drones.erase(it); drones.insert(itt);
            //actualizo orden + score si complete
            ordenes.erase( ordenes.begin() );
            if(vacia){ //gane
                score += (int)(((ld)itt.t / (ld)T)*100);
            }else{// no gane
                ordenes.insert( orden_actual );
            }
            //si el warehouse quedo vacio lo borro
            bool vacio = true;
            for(auto &p : warehouses[best_warehouse].item_times) if(p.second!=0){vacio=false;break;}
            if(vacio){swap(warehouses[best_warehouse],warehouses.back());warehouses.pop_back();}
        }
    }
    cerr<< score <<endl;
}