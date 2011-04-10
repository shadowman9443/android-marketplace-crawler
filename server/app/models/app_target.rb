class AppTarget < ActiveRecord::Base
  belongs_to :app
  belongs_to :target
end
