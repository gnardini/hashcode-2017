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

int rand(int a, int b){
    mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());
    return rng()%(b-a+1) + a;
}

struct elem{
    int id, idd;
    set<string> tags;
    bool operator<(elem &other)const{return tags.size()>other.tags.size();}
};

int score(elem &a, elem &b){
    set<string> a_b = a.tags; for(auto &it: b.tags) a_b.erase(it);
    set<string> b_a = b.tags; for(auto &it: a.tags) b_a.erase(it);
    set<string> ab; for(auto &it: a.tags) if(b.tags.count(it)) ab.insert(it);
    return min(a_b.size(),min(b_a.size(),ab.size()));
}

int main(){
    IOS;
    int M;cin>>M;
    vec<elem> horizontals;
    vec<elem> verticals;
    for(int i=0;i<M;i++){
        char c; int n; set<string> tags;
        cin>>c; cin>>n; while(n--){string t;cin>>t; tags.insert(t);}
        if(c=='H') horizontals.push_back({i,-1, tags});
        else verticals.push_back({i,-1, tags});
    }
    sort(all(horizontals));
    list<elem> h(all(horizontals));
    horizontals = {};
    while(h.size()){ horizontals.push_back(h.front()); h.pop_front(); if(h.size()){horizontals.push_back(h.back());h.pop_back();} }
    if(horizontals.size()%2==1) horizontals.pop_back();

    vec<elem> horizontals_merged;
    for(int i=0;i<horizontals.size();i+=2){
        set<string> merge = horizontals[i].tags; for(auto &it: horizontals[i+1].tags) merge.insert(it);
        horizontals_merged.push_back( {horizontals[i].id,horizontals[i+1].id,merge} );
    }

    vec<int> horizontals_to_visit;
    for(int i=0;i<horizontals_merged.size();i++) horizontals_to_visit.push_back(i);
    vec<int> verticals_to_visit;
    for(int i=0;i<verticals.size();i++) verticals_to_visit.push_back(i);
    random_shuffle(all(verticals_to_visit));
    list<elem> solution;
    pair<elem,int> best_start = {{0,-1,{}},0};
    for(auto it: verticals_to_visit) if(  verticals[ it ].tags.size() > best_start.first.tags.size() ) best_start = {verticals[it], it};
    for(auto it: horizontals_to_visit) if(  horizontals_merged[ it ].tags.size() > best_start.first.tags.size() ) best_start = {horizontals_merged[it], it};
    if(best_start.first.idd==-1){ //vertical
        swap( verticals_to_visit.back(), verticals_to_visit[ best_start.second ] );
        verticals_to_visit.pop_back();
    }else{ //horizontal
        swap( horizontals_to_visit.back(), horizontals_to_visit[ best_start.second ] );
        horizontals_to_visit.pop_back();
    }

    if(rand(1,100)<=50 && verticals_to_visit.size()){
        solution.push_back( verticals[ verticals_to_visit.back() ] );
        verticals_to_visit.pop_back();
    }else{
        solution.push_back( horizontals_merged[ horizontals_to_visit.back() ] );
        horizontals_to_visit.pop_back();
    } // maybe start from a special elem? Max tags for example

    while(horizontals_to_visit.size() || verticals_to_visit.size()){ // O(M)
        int d = horizontals_to_visit.size() + verticals_to_visit.size();
        if( d%1000 == 0 ) cerr<<d<<endl;
        vec<pair<elem,int>> candidates_index;
        for(int i=0;i*i<=M;i++){ // O(sqrt(M))
            if(horizontals_to_visit.size()) {
                int r = rand(0, horizontals_to_visit.size()-1);
                candidates_index.push_back( {horizontals_merged[ horizontals_to_visit[r] ], r} );
            }
            if(verticals_to_visit.size()) {
                int r = rand()%verticals_to_visit.size();//rand(0, verticals_to_visit.size()-1);
                candidates_index.push_back( {verticals[ verticals_to_visit[r] ], r} );
            }
        }
        elem left = *solution.begin();
        elem right = *prev(solution.end());
        pair<elem,int> best = candidates_index[0];
        int best_score = max( score(best.first, left), score(best.first, right) );
        for(auto &c : candidates_index){
            int this_score = max( score(c.first, left), score(c.first, right) );
            if( this_score > best_score ){
                best = c; best_score = this_score;
            }
        }
        int score_left =  score(best.first,left);
        int score_right = score(best.first, right);
        if(best.first.idd==-1){ //vertical
            if( score_left > score_right ) solution.push_front( best.first );
            else solution.push_back( best.first );
            swap( verticals_to_visit.back(), verticals_to_visit[ best.second ] );
            verticals_to_visit.pop_back();
        }else{ //horizontal
            if( score_left > score_right ) solution.push_front( best.first );
            else solution.push_back( best.first );
            swap( horizontals_to_visit.back(), horizontals_to_visit[ best.second ] );
            horizontals_to_visit.pop_back();
        }
    }

    cout<< solution.size() << endl;
    for(auto &it: solution){
        cout<< it.id; if(it.idd!=-1) cout<<" "<<it.idd;
        cout<<endl;
    }

    ll ans = 0;
    auto it = solution.begin(); it++;
    for(;it!=solution.end();it++) ans += score( *prev(it), *it );
    cerr<<ans<<endl;
}

/*
4
H 3 cat beach sun
V 2 selfie smile
V 2 garden selfie
H 2 garden cat
 */
