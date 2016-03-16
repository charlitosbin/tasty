package com.tasty.placesapi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Tasty_PlacesAPI for Distributed Systems Project  EEEV  11/03/2016
 */
public class AutoCompleteAdapter extends ArrayAdapter<AutoCompletePlace> {

    private GoogleApiClient mGoogleApiClient;

    public AutoCompleteAdapter( Context context ) {
        super(context, 0);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        ViewHolder holder;

        if( convertView == null ) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from( getContext() ).inflate( android.R.layout.simple_list_item_1, parent, false  );
            holder.text = (TextView) convertView.findViewById( android.R.id.text1 );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText( getItem( position ).getDescription() );

        return convertView;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
    }

    private class ViewHolder {
        TextView text;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() ) {
                    Toast.makeText( getContext(), "No conectado", Toast.LENGTH_SHORT ).show();
                    return null;
                }

                clear();

                displayPredictiveResults( constraint.toString() );

                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private void displayPredictiveResults( String query )
    {
        // Incluye Guadalajara, Zapopan y Tlaquepaque.
        LatLngBounds bounds = new LatLngBounds( new LatLng( 20.639708, -103.311846 ), new LatLng( 20.671956, -103.416501 ) );

        // Filtro de Tipos: https://developers.google.com/places/supported_types#table3
        List<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add( Place.TYPE_RESTAURANT );

        Places.GeoDataApi.getAutocompletePredictions( mGoogleApiClient, query, bounds, AutocompleteFilter.create( filterTypes ) )
            .setResultCallback (
                new ResultCallback<AutocompletePredictionBuffer>() {
                    @Override
                    public void onResult( AutocompletePredictionBuffer buffer ) {

                        if( buffer == null )
                            return;

                        if( buffer.getStatus().isSuccess() ) {
                            for( AutocompletePrediction prediction : buffer ) {
                                // Añadir como un nuevo elemento para evitar IllegalArgumentsException cuando el buffer sea liberado
                                add( new AutoCompletePlace( prediction.getPlaceId(), prediction.getDescription() ) );
                            }
                        }

                        // Prevenir pérdida de memoria liberando el buffer
                        buffer.release();
                    }
                }, 60, TimeUnit.SECONDS );
    }
}
